export const hdctSuccessRequest = {
  formulaVersion: 'approved-v1',
  inputs: {
    directCost: 1000,
    indirectCost: 250,
    throughputUnits: 100
  }
};

export const hdctInvalidRequest = {
  formulaVersion: 'approved-v1',
  inputs: {
    directCost: 'bad-value'
  }
};
