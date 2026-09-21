# Marketplace rebrand design

## Purpose

Prepare the author's internship services for presentation as a cohesive
Marketplace laboratory project.  The rebrand removes organization and
internship product identity from live source, build metadata, deployment
configuration, documentation, and repository names while preserving code
behaviour and Git authorship/history.

## Repository names

| Current repository | New repository |
| --- | --- |
| `Innowise-Internship` | `marketplace` |
| `Internship-Payment-Service` | `marketplace-payment-service` |
| `Intership-User-Service` | `marketplace-user-service` |
| `Internship-Auth-Service` | `marketplace-auth-service` |
| `Internship-Api-Gateway` | `marketplace-api-gateway` |
| `Internship-Order-Service` | `marketplace-order-service` |
| `Internship-K8S` | `marketplace-k8s` |

The user renames remote GitHub repositories through their authenticated GitHub
account.  Local `origin` URLs are updated only after the matching remote names
exist.

## Replacement rules

Apply the following transformations in tracked, live project files:

| Existing identity | Replacement |
| --- | --- |
| `Innowise` | `Marketplace` |
| `innowise` | `marketplace` |
| `com.innowise` | `com.marketplace` |
| `internship` in project/product identifiers | `marketplace` |

The exact casing and delimiters are preserved where required by an identifier.
Package directory moves accompany Java/Kotlin package declarations, and all
affected imports are updated in source and tests.

## In-scope surfaces

- build metadata and module identifiers (Maven, Gradle, npm, and similar);
- source package names and imports;
- service names, discovery/configuration keys, and API Gateway routes where
  they contain the old product identity;
- Dockerfiles, Compose files, image names, tags, and container environment;
- Kubernetes namespaces, labels, selectors, ConfigMaps, Secrets references,
  Kustomize/Helm values, image references, and inter-service URLs;
- README files, examples, scripts, CI definitions, and repository links;
- each local Git `origin` URL once its GitHub repository has been renamed.

## Explicit exclusions

- application logic, public API behaviour, database schemas, and service
  contracts, except for name-only wiring needed to keep deployments working;
- Git commit history, commit messages, authors, and license/copyright notices;
- destructive branch rewrites or force-pushes;
- deployment to a Kubernetes cluster or publication of changes to GitHub.

## Compatibility and validation

Each repository is inspected before edits to identify its build and deployment
tooling.  After its rebrand, run the project-native test/build command when
available, scan tracked files for prohibited live identifiers, and validate
Kubernetes manifests with client-side/dry-run tooling when installed.  Findings
that cannot be automatically corrected, such as an external secret name or a
hard-coded third-party hostname, are reported with the exact file and required
decision.

## Completion criteria

1. All seven local projects use their Marketplace names and working local
   remotes.
2. No live tracked file contains `innowise`, `Innowise`, or an old
   internship product identifier, apart from intentionally preserved Git
   history or legally required notices.
3. Packages, build metadata, deployment manifests, documentation, and
   inter-service references agree on the new names.
4. Applicable builds/tests and manifest checks have recorded results.
