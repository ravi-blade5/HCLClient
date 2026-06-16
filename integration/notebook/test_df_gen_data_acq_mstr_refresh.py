import pytest

def transform_record(record):
    return {
        "record_id": record["record_id"].strip(),
        "status": record.get("status", "READY").upper(),
        "source_system": record.get("source_system", "unknown").lower()
    }

def should_retry(run_state):
    return run_state.get("status") == "FAILED" and run_state.get("attempts", 0) < 3

def test_transform_record_normalizes_fields():
    record = {
        "record_id": "  abc123  ",
        "status": "ready",
        "source_system": "ADF"
    }
    transformed = transform_record(record)
    assert transformed["record_id"] == "abc123"
    assert transformed["status"] == "READY"
    assert transformed["source_system"] == "adf"

def test_transform_record_defaults_missing_status():
    record = {
        "record_id": "x1",
        "source_system": "Synapse"
    }
    transformed = transform_record(record)
    assert transformed["status"] == "READY"

def test_should_retry_for_failed_runs():
    assert should_retry({"status": "FAILED", "attempts": 1}) is True
    assert should_retry({"status": "FAILED", "attempts": 3}) is False
    assert should_retry({"status": "SUCCEEDED", "attempts": 1}) is False
