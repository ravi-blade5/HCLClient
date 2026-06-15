import request from 'supertest';
import { app } from '../../services/planning-actuals-api/src/server-test-harness';

describe('CHG00645486 API integration', () => {
  it('returns planning actuals envelope', async () => {
    const response = await request(app).get('/api/planning-actuals?viewType=planning');
    expect(response.status).toBe(200);
    expect(response.body).toMatchObject({
      status: 'success',
      correlationId: expect.any(String)
    });
    expect(Array.isArray(response.body.data)).toBe(true);
  });

  it('rejects invalid planning actuals viewType', async () => {
    const response = await request(app).get('/api/planning-actuals?viewType=invalid');
    expect(response.status).toBe(400);
    expect(response.body.status).toBe('error');
  });

  it('returns comparison data for Other Forecast', async () => {
    const response = await request(app).get('/api/comparison/other-forecast');
    expect(response.status).toBe(200);
    expect(response.body.data[0]).toHaveProperty('datasetType', 'other-forecast');
  });

  it('returns comparison detail by record id', async () => {
    const response = await request(app).get('/api/comparison/other-forecast/details/other-forecast-001');
    expect(response.status).toBe(200);
    expect(response.body.data).toHaveProperty('recordId', 'other-forecast-001');
  });

  it('returns HDCT calculation output', async () => {
    const response = await request(app)
      .post('/api/calculations/hdct')
      .send({
        formulaVersion: 'approved-v1',
        inputs: {
          directCost: 1000,
          indirectCost: 250,
          throughputUnits: 100
        }
      });

    expect(response.status).toBe(200);
    expect(response.body.data).toHaveProperty('formulaVersion', 'approved-v1');
  });

  it('rejects invalid HDCT input', async () => {
    const response = await request(app)
      .post('/api/calculations/hdct')
      .send({
        formulaVersion: 'approved-v1',
        inputs: {
          directCost: 'bad-value'
        }
      });

    expect(response.status).toBe(400);
    expect(response.body.status).toBe('error');
  });

  it('returns executive summary data', async () => {
    const response = await request(app).get('/api/executive-summary');
    expect(response.status).toBe(200);
    expect(response.body).toHaveProperty('correlationId');
  });

  it('returns planning periods', async () => {
    const response = await request(app).get('/api/planning/periods');
    expect(response.status).toBe(200);
    expect(Array.isArray(response.body.data)).toBe(true);
  });

  it('initializes next year dataset', async () => {
    const response = await request(app).post('/api/planning/datasets/next-year');
    expect(response.status).toBe(200);
    expect(response.body.data).toHaveProperty('isBlankDataset', true);
  });
});
