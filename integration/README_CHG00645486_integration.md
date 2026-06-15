# CHG00645486 Integration Test Execution Notes

## Scope
This package validates the Planning vs Actuals solution across SPA, API, SQL, and notebook integration layers.

## Test Categories
- Playwright SPA-to-API integration
- Jest + Supertest API integration
- SQL validation script execution
- Pytest notebook/helper integration

## Environment Variables
- PLAYWRIGHT_WEB_BASE_URL
- PLAYWRIGHT_API_BASE_URL
- API_BASE_URL
- NODE_ENV
- SQL connection variables if SQL tests are run against a live environment

## Execution Order
1. API integration tests
2. SPA integration tests
3. SQL validation script
4. Notebook/helper tests

## Traceability
- Epics: AIF-92 to AIF-95
- Stories: AIF-96 to AIF-107
- Requirements: FR-01 to FR-17, NFR-01 to NFR-07
