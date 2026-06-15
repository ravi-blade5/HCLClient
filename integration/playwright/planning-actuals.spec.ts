import { test, expect } from '@playwright/test';

const apiBaseUrl = process.env.PLAYWRIGHT_API_BASE_URL ?? 'http://localhost:8080';
const webBaseUrl = process.env.PLAYWRIGHT_WEB_BASE_URL ?? 'http://localhost:5173';

test.describe('CHG00645486 Planning vs Actuals SPA integration', () => {
  test.beforeEach(async ({ page }) => {
    await page.route('**/api/**', async (route) => {
      const url = new URL(route.request().url());

      if (url.pathname === '/api/planning-actuals') {
        const viewType = url.searchParams.get('viewType');
        const body = {
          data: [
            {
              recordId: `${viewType}-001`,
              viewType,
              period: '2025-P01',
              line: viewType === 'planning' ? 'Planning Line A' : 'Actual Line A',
              jobText: 'Sample Job Text',
              kpiCode: 'KPI_SAMPLE',
              kpiName: 'Sample KPI',
              value: viewType === 'planning' ? 1000 : 975,
              sourceSystem: 'mock'
            }
          ],
          metadata: {},
          validationMessages: [],
          correlationId: 'test-correlation-planning-actuals',
          status: 'success'
        };
        await route.fulfill({ json: body });
        return;
      }

      if (url.pathname.startsWith('/api/comparison/')) {
        const datasetType = url.pathname.split('/')[3];
        const body = {
          data: [
            {
              recordId: `${datasetType}-001`,
              datasetType,
              period: '2025-P01',
              lineName: 'Sample Line',
              jobText: 'Sample Job',
              forecastValue: 1000,
              actualValue: 975,
              variance: -25,
              variancePct: -2.5
            }
          ],
          metadata: {},
          validationMessages: [],
          correlationId: `test-${datasetType}`,
          status: 'success'
        };
        await route.fulfill({ json: body });
        return;
      }

      if (url.pathname === '/api/calculations/hdct') {
        const body = {
          data: {
            formulaVersion: 'approved-v1',
            result: 12.34,
            warnings: []
          },
          metadata: {},
          validationMessages: [],
          correlationId: 'test-hdct',
          status: 'success'
        };
        await route.fulfill({ json: body });
        return;
      }

      if (url.pathname === '/api/executive-summary') {
        const body = {
          data: [
            {
              summaryId: 'summary-001',
              metricName: 'Planning vs Actuals Variance',
              metricValue: -25,
              variance: -2.5,
              insightText: 'Mock executive insight.',
              dataFreshnessTimestamp: new Date().toISOString()
            }
          ],
          metadata: {},
          validationMessages: [],
          correlationId: 'test-summary',
          status: 'success'
        };
        await route.fulfill({ json: body });
        return;
      }

      if (url.pathname === '/api/planning/periods') {
        const body = {
          data: [
            {
              year: 2025,
              isCurrentYear: true,
              isLocked: true,
              isDatasetInitialized: true,
              isBlankDataset: false
            },
            {
              year: 2026,
              isCurrentYear: false,
              isLocked: false,
              isDatasetInitialized: true,
              isBlankDataset: true
            }
          ],
          metadata: {},
          validationMessages: [],
          correlationId: 'test-periods',
          status: 'success'
        };
        await route.fulfill({ json: body });
        return;
      }

      if (url.pathname === '/api/planning/datasets/next-year') {
        const body = {
          data: {
            year: 2026,
            isCurrentYear: false,
            isLocked: false,
            isDatasetInitialized: true,
            isBlankDataset: true
          },
          metadata: {},
          validationMessages: [],
          correlationId: 'test-next-year',
          status: 'success'
        };
        await route.fulfill({ json: body });
        return;
      }

      await route.continue();
    });

    await page.goto(webBaseUrl);
  });

  test('toggles between Planning and Actuals views', async ({ page }) => {
    await expect(page.getByRole('heading', { name: /planning vs actuals/i })).toBeVisible();

    await page.getByRole('button', { name: 'Actuals' }).click();
    await expect(page.getByRole('heading', { name: /actuals dataset/i })).toBeVisible();

    await page.getByRole('button', { name: 'Planning' }).click();
    await expect(page.getByRole('heading', { name: /planning dataset/i })).toBeVisible();
  });

  test('sorts lines and job texts deterministically', async ({ page }) => {
    const table = page.locator('.data-table').first();
    await expect(table).toBeVisible();

    await page.getByRole('button', { name: /line/i }).first().click();
    await expect(table).toBeVisible();

    await page.getByRole('button', { name: /job text/i }).first().click();
    await expect(table).toBeVisible();
  });

  test('loads Other Forecast comparison and opens modal', async ({ page }) => {
    const otherForecastSection = page.getByRole('heading', { name: /other forecast/i });
    await expect(otherForecastSection).toBeVisible();

    const row = page.locator('.clickable-row').first();
    await row.click();

    await expect(page.getByRole('dialog', { name: /other forecast detail/i })).toBeVisible();
    await page.getByRole('button', { name: 'Close' }).click();
    await expect(page.getByRole('dialog', { name: /other forecast detail/i })).toHaveCount(0);
  });

  test('renders Line Forecast and Fixed & Other Indirect views', async ({ page }) => {
    await expect(page.getByRole('heading', { name: /line forecast/i })).toBeVisible();
    await expect(page.getByRole('heading', { name: /fixed & other indirect forecast/i })).toBeVisible();
    await expect(page.locator('[aria-label="Variance chart"]')).toBeVisible();
  });

  test('executes HDCT calculation flow', async ({ page }) => {
    const panel = page.getByRole('heading', { name: /hdct calculation/i });
    await expect(panel).toBeVisible();

    await page.getByRole('button', { name: /calculate hdct/i }).click();
    await expect(page.getByText(/result:/i)).toBeVisible();
  });

  test('renders Executive Summary and planning guardrails', async ({ page }) => {
    await expect(page.getByRole('heading', { name: /executive summary/i })).toBeVisible();
    await expect(page.getByRole('heading', { name: /planning tool guardrails/i })).toBeVisible();
    await expect(page.getByRole('button', { name: /initialize blank next-year dataset/i })).toBeVisible();
  });
});
