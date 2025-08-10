import type { Config } from "jest";

const config: Config = {
  clearMocks: true,
  coverageProvider: "v8",
  rootDir: "./src",
  setupFilesAfterEnv: ["./shared/infra/testing/expect-helpers.ts"],
  transform: {
    "^.+\\.(t|j)sx?$": "@swc/jest",
  },
};

module.exports = config;
