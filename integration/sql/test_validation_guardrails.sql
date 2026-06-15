-- CHG00645486 SQL integration validation
-- Traceability: AIF-105, AIF-106, AIF-107
-- Requirements: FR-08, FR-16, FR-17, NFR-01, NFR-02, NFR-03, NFR-06

SET NOCOUNT ON;

DECLARE @failures TABLE (
  test_name nvarchar(200),
  failure_reason nvarchar(max)
);

IF OBJECT_ID(N'PBNA_MLF.Activity_Log', N'U') IS NULL
  INSERT INTO @failures VALUES (N'Activity_Log exists', N'PBNA_MLF.Activity_Log not found');

IF OBJECT_ID(N'PBNA_MLF.Error_Log', N'U') IS NULL
  INSERT INTO @failures VALUES (N'Error_Log exists', N'PBNA_MLF.Error_Log not found');

IF OBJECT_ID(N'PBNA_MLF.PlanningPeriodControl', N'U') IS NULL
  INSERT INTO @failures VALUES (N'PlanningPeriodControl exists', N'PBNA_MLF.PlanningPeriodControl not found');

IF OBJECT_ID(N'PBNA_MLF.usp_ValidateActualsReconciliation', N'P') IS NULL
  INSERT INTO @failures VALUES (N'ValidateActualsReconciliation proc exists', N'Procedure not found');

IF OBJECT_ID(N'PBNA_MLF.usp_InitializeBlankNextYearPlanningDataset', N'P') IS NULL
  INSERT INTO @failures VALUES (N'Initialize next-year proc exists', N'Procedure not found');

IF EXISTS (SELECT 1 FROM @failures)
BEGIN
  SELECT * FROM @failures;
  THROW 51000, 'CHG00645486 SQL integration validation failed.', 1;
END

PRINT 'CHG00645486 SQL integration validation passed.';
