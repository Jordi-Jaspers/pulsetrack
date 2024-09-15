import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { keycloakify } from "keycloakify/vite-plugin";
import path from "path";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        react(),

        keycloakify({
            themeName: "pulsetrack_theme",
            accountThemeImplementation: "none",
            keycloakifyBuildDirPath: "../../dist/apps/keycloak-theme",
            keycloakVersionTargets: {
                hasAccountTheme: true,
                "21-and-below": false,
                "23": false,
                "24": false,
                "25-and-above": "pulsetrack_theme.jar"
            }
        })
    ],
    resolve: {
        alias: {
            "@": path.resolve(__dirname, "./src")
        }
    }
});
