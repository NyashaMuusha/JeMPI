export type Config = {
  isDev: boolean
  apiUrl: string
  shouldMockBackend: boolean
  KeyCloakUrl: string
  KeyCloakRealm: string
  KeyCloakClientId: string
  useSso: boolean
  maxUploadCsvSize: number
  showBrandLogo: boolean
}

export default async function getConfig() {
  try {
    const res = await fetch('/config.json')
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const conf = (await res.json()) as any
    return {
      isDev: conf.nodeEnv !== 'production',
      apiUrl: conf.apiHost
        ? `${conf.apiHost}:${conf.apiPort}`
        : `${window.location.protocol}//${window.location.hostname}:${conf.apiPort}`,
      shouldMockBackend: conf.shouldMockBackend,
      KeyCloakUrl: conf.KeyCloakUrl,
      KeyCloakRealm: conf.KeyCloakRealm,
      KeyCloakClientId: conf.KeyCloakClientId,
      useSso: conf.useSso,
      maxUploadCsvSize: conf.maxUploadCsvSize,
      showBrandLogo: conf.showBrandLogo
    } as Config
  } catch {
    // eslint-disable-next-line no-console
    console.warn(
      'Unable to fetch the config json file, either some env var are missing or Jempi UI is running in development mode (yarn start)'
    )
    return {
      isDev: process.env.NODE_ENV !== 'production',
      apiUrl: process.env.REACT_APP_JEMPI_BASE_API_HOST
        ? `${process.env.REACT_APP_JEMPI_BASE_API_HOST}:${process.env.REACT_APP_JEMPI_BASE_API_PORT}`
        : `${window.location.protocol}//${window.location.hostname}:${process.env.REACT_APP_JEMPI_BASE_API_PORT}`,
      shouldMockBackend: process.env.REACT_APP_MOCK_BACKEND === 'true',
      KeyCloakUrl: process.env.KC_FRONTEND_URL || 'http://localhost:9088',
      KeyCloakRealm: process.env.KC_REALM_NAME || 'platform-realm',
      KeyCloakClientId: process.env.KC_JEMPI_CLIENT_ID || 'jempi-oauth',
      useSso: process.env.REACT_APP_ENABLE_SSO === 'true',
      maxUploadCsvSize: +(
        process.env.REACT_APP_MAX_UPLOAD_CSV_SIZE_IN_MEGABYTES || 128
      ),
      showBrandLogo: process.env.REACT_APP_SHOW_BRAND_LOGO === 'true'
    }
  }
}
