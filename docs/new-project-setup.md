## New project setup in team emottak (using this repo as template)

This guide shows how to bootstrap a new Kotlin/Ktor service in team emottak by copying the structure from this project. It includes NAIS config, logging, tracing, metrics, Gradle, and a minimal Ktor app with health/metrics endpoints.

### Prerequisites
- Java 21 (Temurin recommended)
- Docker (for image build/run)
- GitHub access to `navikt` org and packages (GITHUB_TOKEN)
- NAIS CLI and cluster access (dev-gcp/prod-gcp)

### Create a new GitHub repository (PUBLIC, MIT)
- Create a new repository under `navikt/your-new-app`.
- Set repository visibility to PUBLIC.
- Do not push or add anything yet; you'll first register the repo in NAIS console (see the NAIS console step).

### Copy this project at a known-good commit
Using the commit you referenced ensures a minimal, working baseline:

```bash
git remote add template https://github.com/navikt/emottak-edi-inbound-service.git
git fetch template 129bef8a07c966b00bb13a6a1f1172f6f2cb2ce2
git checkout FETCH_HEAD -- .
```

Alternatively, download the repo directly at that commit and copy the files into your new repo. 

If you decide to clone the project, remember to delete the `.git` folder before you run `git init`.

### Rename identifiers to your app
Perform a careful search-and-replace. Use consistent naming for all places below:

- Replace app/module names
  - `settings.gradle.kts` → update `rootProject.name` from `emottak-edi-inbound-service` to your own app name.
  - `.github/workflows/edi-inbound-service-deploy-dev.yaml` → update `MODULE_NAME` and `NAIS_MANIFEST` env values and rename the github workflow file.
  - `README.md` title and any references.

- Package and Kotlin main class
  - Source root is under `src/main/kotlin/no/nav/emottak/...`. Create or rename the package path to match your domain, e.g. `.../no/nav/emottak/<your-domain>/<your-app>`.
  - If you move `App.kt` anywhere else besides `no.nav.emottak`, you need to also update `application { mainClass.set("no.nav.emottak.AppKt") }` in `build.gradle.kts` to point to your new `AppKt` location (e.g. `no.nav.emottak.<yourdomain>.<yourapp>.AppKt`).
  - Move/rename `src/main/kotlin/no/nav/emottak/App.kt` and any packages under `no/nav/emottak/edi/inbound` to your chosen structure.

- NAIS manifest(s)
  - `.nais/*.yaml`:
    - `metadata.name`: change from `emottak-edi-inbound-service` to your app.
    - `spec.ingresses`: set appropriate internal/external hosts.
    - `spec.accessPolicy`: update inbound/outbound rules to match your dependencies/consumers.
    - Keep `namespace: team-emottak` and `labels.team: team-emottak` unless otherwise required.
  - If you add prod later, duplicate the dev file and adjust cluster-specific values.

- Docker/image
  - Dockerfile is generic (distroless Java 21). No change required, but ensure workflow and NAIS reference the correct image output.


### Configure GitHub Actions (deploy to dev)
Open `.github/workflows/edi-inbound-service-deploy-dev.yaml` and set your app-specific values (`MODULE_NAME`, `NAIS_MANIFEST`, optionally rename the workflow file). 

> When you push for the first time to `main`, GitHub Actions will run and deploy the app to `dev-gcp` via NAIS using your manifest.

### Register repository in NAIS console (before first push)
Add the new repository to the team’s NAIS console so deployments are allowed:

- Open the team repositories page: [team-emottak repositories](https://console.nav.cloud.nais.io/team/team-emottak/repositories)
- Add `navikt/your-new-app` to the list.

Only after this step, proceed with the initial push to GitHub.

### Observability and security (optional)
- Tracing/logging/metrics
  - OpenTelemetry auto-instrumentation is enabled in NAIS (`observability.autoInstrumentation`). Logs go to Elastic/Loki per manifest.
  - Prometheus metrics exposed at `/prometheus`.

- Auth config
  - Manifest enables `tokenx` and Azure AD application by default; tailor to your needs.
  - Update `accessPolicy` to allow only the necessary callers and outbound access.

- Secrets
  - Example Vault mount is present; replace with your own mounts/paths as needed.


### Initialize git, set main, first commit, and push
From your local working copy (after renames):

```bash
git init
git branch -M main
git add .
git commit -m "feat: first commit"
git remote add origin git@github.com:navikt/your-new-app.git
# Register repo in NAIS console BEFORE pushing (see the NAIS console step)
git push -u origin main
```

### First deployment checklist
- [ ] Build succeeds locally with `./gradlew build`
    - Run `./gradlew ktlintFormat ktlintTestFormat` to fix formatting errors, if any.
- [ ] App starts locally (`./gradlew run`)
- [ ] `.nais/<your-app>-dev.yaml` updated (name, ingresses, policies)
- [ ] GitHub workflow updated (module name, manifest path)
- [ ] Repository is PUBLIC and carries MIT license
- [ ] Repo added in NAIS console team repositories
- [ ] Repo has `GITHUB_TOKEN` available for builds (default in Actions)
- [ ] Merge to `main` → verify image is built and app deploys to `dev-gcp`
- [ ] Test deployed ingress healthcheck endpoint!

### Quick test after first deployment
- After the GitHub Actions job completes, open your app’s ingress URL and hit the health endpoints:
  - `<your-ingress>/internal/health/liveness` → returns a simple text message (e.g., "I'm alive! :)")
  - `<your-ingress>/internal/health/readiness` → returns a simple text message (e.g., "I'm ready! :)")
  - If both return 200 with text, the app is up in dev.

### Prod: similar setup
- Duplicate the dev NAIS manifest for prod (adjust `ingresses`, resources, and policies) and create a prod workflow mirroring the dev one but deploying to `prod-fss` (we may want to use `gcp` in the future).

### After bootstrap
- Add proper README with domain context and API docs.
- Tighten `accessPolicy` (principle of least privilege).
- Add prod manifest and a prod workflow when ready.
- Add alerts/dashboards as needed in Grafana.

---

Tip: When renaming packages and the `AppKt` entrypoint, ensure `application.mainClass` in `build.gradle.kts` matches the new fully qualified name; otherwise the container won’t start.


