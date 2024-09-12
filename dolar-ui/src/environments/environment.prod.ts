import { DEFAULT_ENV } from "./environment.default";
import { EnvironmentModel } from "./environment.model";

const env = "env" in window ? window["env"] as EnvironmentModel : DEFAULT_ENV

export const environment: EnvironmentModel = {
    production: true,
    apiUrl: env?.apiUrl || "default",
};
