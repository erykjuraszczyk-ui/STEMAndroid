# STEMAndroid

Fanowski launcher i menedżer biblioteki gier Steam na Androida 13+.

**Wersja:** 0.1.0 (MVP)  
**Platforma:** Android 13+ (API 33), ARM64  
**Język:** Kotlin  
**UI:** Jetpack Compose

## Założenie projektu

STEMAndroid porządkuje bibliotekę gier Windows kupionych na Steam i zapewnia dwa sposoby grania:

- **Lokalnie — Winlator:** gry Windows mogą działać na urządzeniu Android przez zewnętrzną warstwę zgodności Wine + Box86/Box64. STEMAndroid otwiera zainstalowany Winlator i kopiuje zapisaną ścieżkę EXE.
- **Zdalnie — Steam Link:** obraz i dźwięk gry są przesyłane z komputera, na którym działa Steam. Gra wykonuje się na komputerze, a telefon służy jako ekran i kontroler.

## Ważne ograniczenie wersji 0.1.0

To jest działający szkielet aplikacji i menedżer biblioteki, a nie kompletna implementacja klienta Steam ani własny emulator Windows. W tej wersji:

- można dodawać gry, Steam App ID i ścieżkę EXE w kontenerze Windows;
- dane biblioteki są zapisywane lokalnie na telefonie;
- można otworzyć kartę gry w Steam;
- można uruchomić Winlator i skopiować ścieżkę EXE do schowka;
- można otworzyć oficjalny Steam Link.

**Automatyczne pobieranie depotów Steam i bezpośrednie uruchamianie EXE jednym kliknięciem nie są jeszcze zaimplementowane.** Nie ma stabilnego, zweryfikowanego publicznego API Winlator, na którym można bezpiecznie oprzeć takie wywołanie. Nie zapisujemy haseł Steam, kodów Steam Guard ani tokenów sesji.

## Wymagania

- Android 13 lub nowszy.
- Urządzenie ARM64; GPU i sterowniki Vulkan zgodne z konkretną grą.
- Winlator zainstalowany osobno dla trybu lokalnego.
- Komputer z uruchomionym Steam oraz Steam Link na telefonie dla trybu zdalnego.
- JDK 17 i Android SDK 35 do kompilacji.

## Budowanie APK

Otwórz katalog repozytorium w Android Studio, poczekaj na synchronizację Gradle, a następnie wybierz **Build → Build APK(s)**.

Konfiguracja projektu używa Android Gradle Plugin 8.7.3, Gradle 8.9, Kotlin 2.0.21 i SDK 35.

GitHub Actions buduje app-debug.apk po każdym pushu do gałęzi main. APK jest publikowany jako artefakt workflow.

## Instalowanie gry lokalnie — obecny przepływ

1. Zainstaluj Winlator z jego oficjalnego źródła i utwórz kontener Windows.
2. Zainstaluj Steam wewnątrz kontenera Winlator, korzystając z oficjalnego instalatora Steam.
3. Zaloguj się wyłącznie w oficjalnym kliencie Steam uruchomionym w kontenerze i pobierz grę, którą posiadasz.
4. W STEMAndroid dodaj grę, jej Steam App ID oraz ścieżkę EXE widoczną wewnątrz kontenera, np. C:\Program Files (x86)\Steam\steamapps\common\NazwaGry\game.exe.
5. Wybierz **Otwórz Winlator**. STEMAndroid otworzy środowisko i skopiuje ścieżkę do schowka. W tej wersji uruchom plik z poziomu Winlatora.

## Licencje i niezależność

STEMAndroid jest projektem fanowskim, niezwiązanym z Valve. Nie zawiera plików Steam, Wine ani Winlator. Użytkownik instaluje wymagane komponenty oddzielnie i korzysta z własnego konta oraz legalnie posiadanych gier. Znaki towarowe należą do ich właścicieli.

## Plan rozwoju

Zobacz docs/ROADMAP.md i docs/ARCHITECTURE.md.
