# AGENTS Guide for ProjectPAMT

## Baseline context
- Existing AI instruction files were searched via glob and none were found (`README.md`, `AGENT*.md`, `.github/copilot-instructions.md`, Cursor/Windsurf/Cline rules).
- This is a single-module Android app (`:app`) using Kotlin + Jetpack Compose + Navigation + Supabase Auth (`settings.gradle.kts`, `app/build.gradle.kts`).
- Supabase is the primary backend for this project, and the SQL schema provided by the user (tables `produk`, `pelanggan`, `kas`, `pengeluaran`, `penjualan`, `detail_penjualan`, `log_inventory`, `log_kas`, `log_pelanggan`, `kategori`) should be treated as the main database context.

## Architecture you need first
- UI flow is Compose-only: `MainActivity` sets `AppNavigation()` as the root (`app/src/main/java/com/example/projectpamt/MainActivity.kt`).
- Auth gate happens before route graph rendering: `AppNavigation` waits on `authCheckState` (`Checking`/`Authenticated`/`NotAuthenticated`) and picks start destination (`navigation/AppNavigation.kt`).
- Main layers are: `ui/*Screen.kt` -> `viewmodel/auth/AuthViewModel.kt` -> `data/repository/AuthRepository.kt` -> `data/SupabaseClientProvider.kt`.
- ViewModel exposes form fields as `StateFlow` (`email`, `password`, `fullname`) and a separate async state (`AuthUiState`), then screens consume with `collectAsStateWithLifecycle` in `MainNavHost`.
- Success navigation is centralized in `LaunchedEffect(uiState)` inside `MainNavHost`; it redirects to dashboard and calls `authViewModel.resetState()`.

## Integration boundaries and external services
- Supabase is the primary backend for authentication and application data; when adding features, keep models and repositories aligned with the user-provided SQL schema.
- Supabase is configured in a singleton provider with `Auth` plugin installed (`data/SupabaseClientProvider.kt`).
- Auth lifecycle depends on Supabase `sessionStatus` flow and `awaitInitialization()` to avoid false logout on cold start (`data/repository/AuthRepository.kt`).
- `register()` sends `full_name` in signup metadata JSON; keep that field key stable unless backend schema changes (`AuthRepository.register`).
- INTERNET permission is required and already declared (`app/src/main/AndroidManifest.xml`).

## Project-specific coding patterns
- Routing uses a sealed class `Screen` with string routes (`navigation/Screen.kt`); add new destinations there first.
- UI text/comments are mixed Indonesian/English; error defaults are Indonesian (e.g., `"Login gagal"`, `"Register gagal"` in `AuthViewModel`). Keep language style consistent in the touched feature.
- Compose screens are mostly stateless and callback-driven (`LoginScreen`, `RegisterScreen`, `DashboardScreen`); keep business logic in ViewModel/repository.
- Current model classes under `data/model/` are plain transport/domain structures; only some are `@Serializable` yet, so check serialization needs per model before using Supabase/PostgREST.

## Build/test workflows (verified constraints)
- Wrapper script is present but not executable in this workspace (`./gradlew` returns permission denied).
- Java runtime is missing in current environment (`sh ./gradlew ...` fails with "Unable to locate a Java Runtime").
- Typical commands once JDK is available:

```bash
chmod +x ./gradlew
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedDebugAndroidTest
```

## Safe change strategy for this repo
- When adjusting auth behavior, update both `AuthCheckState` gating and nav transitions to avoid loops between `Login` and `Dashboard`.
- Prefer extending `AuthRepository` for Supabase calls rather than calling Supabase directly from UI or navigation code.
- Keep `SupabaseClientProvider` as the single source for client config; do not create ad-hoc clients in screens/viewmodels.

## SQL schema (source of truth)
```sql
-- WARNING: This schema is for context only and is not meant to be run.
-- Table order and constraints may not be valid for execution.

CREATE TABLE public.detail_penjualan (
  id_detail_penjualan uuid NOT NULL DEFAULT gen_random_uuid(),
  id_penjualan uuid NOT NULL,
  id_produk uuid NOT NULL,
  kuantitas numeric NOT NULL,
  harga_satuan numeric NOT NULL,
  CONSTRAINT detail_penjualan_pkey PRIMARY KEY (id_detail_penjualan),
  CONSTRAINT detail_penjualan_id_penjualan_fkey FOREIGN KEY (id_penjualan) REFERENCES public.penjualan(id_penjualan),
  CONSTRAINT detail_penjualan_id_produk_fkey FOREIGN KEY (id_produk) REFERENCES public.produk(id_produk)
);
CREATE TABLE public.kas (
  id_kas uuid NOT NULL DEFAULT gen_random_uuid(),
  nama text NOT NULL,
  saldo numeric NOT NULL,
  aktif boolean NOT NULL DEFAULT true,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT kas_pkey PRIMARY KEY (id_kas)
);
CREATE TABLE public.kategori (
  id_kategori uuid NOT NULL DEFAULT gen_random_uuid(),
  nama text NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT kategori_pkey PRIMARY KEY (id_kategori)
);
CREATE TABLE public.log_inventory (
  id_log_inventory uuid NOT NULL DEFAULT gen_random_uuid(),
  id_produk uuid NOT NULL DEFAULT gen_random_uuid(),
  nama_lama text NOT NULL,
  harga_lama numeric NOT NULL,
  stok_lama numeric NOT NULL,
  stok_baru numeric NOT NULL,
  updated_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT log_inventory_pkey PRIMARY KEY (id_log_inventory),
  CONSTRAINT log_inventory_id_produk_fkey FOREIGN KEY (id_produk) REFERENCES public.produk(id_produk)
);
CREATE TABLE public.log_kas (
  id_log_kas uuid NOT NULL DEFAULT gen_random_uuid(),
  id_kas uuid NOT NULL,
  saldo_awal numeric NOT NULL,
  saldo_akhir numeric NOT NULL,
  keterangan text NOT NULL,
  updated_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT log_kas_pkey PRIMARY KEY (id_log_kas),
  CONSTRAINT log_kas_id_kas_fkey FOREIGN KEY (id_kas) REFERENCES public.kas(id_kas)
);
CREATE TABLE public.log_pelanggan (
  id_log_pelanggan uuid NOT NULL DEFAULT gen_random_uuid(),
  id_pelanggan uuid NOT NULL,
  nama_lama text NOT NULL,
  telepon_lama text NOT NULL,
  aktif_lama boolean NOT NULL,
  updated_at timestamp with time zone NOT NULL,
  CONSTRAINT log_pelanggan_pkey PRIMARY KEY (id_log_pelanggan),
  CONSTRAINT log_pelanggan_id_pelanggan_fkey FOREIGN KEY (id_pelanggan) REFERENCES public.pelanggan(id_pelanggan)
);
CREATE TABLE public.pelanggan (
  id_pelanggan uuid NOT NULL DEFAULT gen_random_uuid(),
  nama text NOT NULL,
  telepon text NOT NULL UNIQUE,
  aktif boolean NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT now(),
  CONSTRAINT pelanggan_pkey PRIMARY KEY (id_pelanggan)
);
CREATE TABLE public.pengeluaran (
  id_pengeluaran uuid NOT NULL DEFAULT gen_random_uuid(),
  id_kategori uuid NOT NULL DEFAULT '5602b255-f3d1-4339-86ff-3da58c0437be'::uuid,
  id_kas uuid NOT NULL,
  deskripsi text,
  total numeric NOT NULL,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT pengeluaran_pkey PRIMARY KEY (id_pengeluaran),
  CONSTRAINT pengeluaran_id_kas_fkey FOREIGN KEY (id_kas) REFERENCES public.kas(id_kas),
  CONSTRAINT pengeluaran_id_kategori_fkey FOREIGN KEY (id_kategori) REFERENCES public.kategori(id_kategori)
);
CREATE TABLE public.penjualan (
  id_penjualan uuid NOT NULL DEFAULT gen_random_uuid(),
  id_pelanggan uuid NOT NULL,
  id_kas uuid NOT NULL,
  jumlah_bayar numeric NOT NULL,
  total_harga numeric NOT NULL,
  detail_penjualan jsonb,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  CONSTRAINT penjualan_pkey PRIMARY KEY (id_penjualan),
  CONSTRAINT penjualan_id_kas_fkey FOREIGN KEY (id_kas) REFERENCES public.kas(id_kas),
  CONSTRAINT penjualan_id_pelanggan_fkey FOREIGN KEY (id_pelanggan) REFERENCES public.pelanggan(id_pelanggan)
);
CREATE TABLE public.produk (
  id_produk uuid NOT NULL DEFAULT gen_random_uuid(),
  nama text NOT NULL,
  harga numeric NOT NULL,
  stok numeric NOT NULL,
  nama_satuan text NOT NULL,
  detail_produk jsonb,
  created_at timestamp with time zone NOT NULL DEFAULT (now() AT TIME ZONE 'utc'::text),
  aktif boolean NOT NULL DEFAULT true,
  CONSTRAINT produk_pkey PRIMARY KEY (id_produk)
);
```

