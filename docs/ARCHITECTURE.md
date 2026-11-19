# Architektura STEMAndroid 0.1.0

## Warstwy

- **UI (Jetpack Compose):** biblioteka, formularz dodawania gry, eksport skryptów oraz akcje lokalne i zdalne.
- **GameStorage:** lokalny zapis JSON w prywatnych preferencjach aplikacji.
- **SteamBatchScripts:** tworzenie skryptów BAT z żądaniami Steam URI.
- **RunnerActions:** otwieranie zewnętrznych aplikacji i oficjalnych stron.

## Instalacja przez Steam URI

Dla poprawnego numerycznego App ID aplikacja generuje plik BAT z poleceniem:

- instalacja: start "" "steam://install/APP_ID"
- uruchomienie: start "" "steam://run/APP_ID"

Plik należy uruchomić wewnątrz kontenera Windows, gdzie działa oficjalny klient Steam i zarejestrowany jest handler protokołu steam://. W przypadku błędów bezpiecznym fallbackiem jest instalacja/uruchomienie bezpośrednio z biblioteki Steam.

Steamworks opisuje uruchamianie aplikacji przez steam://run/<AppId> w dokumentacji SteamAPI_RestartAppIfNecessary: https://partner.steamgames.com/doc/api/steam_api

## Tryb lokalny

Android nie uruchamia plików PE/Windows EXE natywnie. Wymagane jest środowisko zgodności, np. Winlator (Wine + Box86/Box64) i sterownik graficzny pasujący do urządzenia.

W wersji 0.1.0 STEMAndroid:
1. przechowuje ścieżkę pliku EXE w przestrzeni Windows kontenera;
2. otwiera główną aktywność Winlatora, jeżeli pakiet com.winlator jest zainstalowany;
3. kopiuje ścieżkę do schowka, aby użytkownik mógł uruchomić ją w kontenerze;
4. tworzy opcjonalne pliki BAT uruchamiane ręcznie w kontenerze.

Aplikacja nie zakłada nieudokumentowanego URI ani prywatnego API Winlatora. Bez potwierdzonego interfejsu nie wykonuje automatycznego startu procesu.

## Tryb zdalny

STEMAndroid otwiera oficjalną aplikację Steam Link. Komputer hosta uruchamia grę i koduje strumień audio/wideo; telefon przesyła sterowanie do komputera.

## Prywatność i bezpieczeństwo

- Dane biblioteki pozostają w prywatnym magazynie aplikacji.
- STEMAndroid nie prosi o login, hasło, Steam Guard ani token sesji.
- Do uruchomienia gier potrzebna jest legalna kopia i uprawnienie do gry na koncie Steam użytkownika.
- Aplikacja nie pobiera ani nie dystrybuuje plików gier lub komponentów osób trzecich.

## Kierunek rozwoju

Przed implementacją bezpośredniego startu gry należy wybrać jeden z wariantów:
- oficjalne, udokumentowane API/Intent udostępnione przez runtime;
- utrzymywany fork runtime z własnym, jawnie zdefiniowanym interfejsem IPC;
- adapter do alternatywnego runtime, z testami wersji i komunikatami o niezgodności.

Instalator Steam PC musi działać wewnątrz kontenera z zachowaniem autoryzacji i ochrony konta. Nie należy implementować własnego formularza zbierającego dane logowania Steam.
