# STEMAndroid

Fanowski launcher i menedżer biblioteki gier Steam na Androida 13+.

**Wersja:** 0.1.0 (MVP)  
**Platforma:** Android 13+ (API 33), ARM64  
**Język:** Kotlin  
**UI:** Jetpack Compose

## Założenie projektu

STEMAndroid porządkuje bibliotekę gier Windows kupionych na Steam i zapewnia dwa sposoby grania:

- **Lokalnie — Winlator:** gry Windows mogą działać na urządzeniu Android przez zewnętrzną warstwę zgodności Wine + Box86/Box64.
- **Zdalnie — Steam Link:** gra wykonuje się na komputerze, a telefon odbiera obraz i dźwięk oraz wysyła sterowanie.

## Funkcje wersji 0.1.0

- lokalna biblioteka gier zapisywana na telefonie;
- dodawanie nazwy gry, Steam App ID i ścieżki EXE w kontenerze Windows;
- otwieranie karty gry w Steam Store;
- eksport pliku BAT żądającego instalacji gry przez Steam URI;
- eksport pliku BAT uruchamiającego już zainstalowaną grę przez Steam URI;
- otwieranie Winlatora i kopiowanie ścieżki EXE do schowka;
- otwieranie oficjalnego Steam Link;
- automatyczne budowanie debug APK w GitHub Actions.

### Ważne ograniczenie

STEMAndroid nie jest własnym emulatorem Windows ani klientem Steam. Skrypty BAT trzeba uruchomić **wewnątrz kontenera Winlator**, w którym zainstalowano oficjalnego klienta Steam i zalogowano konto posiadające daną grę. Polecenie instalacji wywołuje protokół Steam — sam skrypt nie pobiera plików gry. Działanie handlera Steam URI zależy od poprawnej instalacji klienta w kontenerze.

Bezpośrednie uruchamianie EXE jednym kliknięciem nie jest jeszcze dostępne: aplikacja otwiera Winlatora i kopiuje ścieżkę do schowka. Nie ma tu niezweryfikowanego, prywatnego API Winlatora.

Nie zapisujemy haseł Steam, kodów Steam Guard ani tokenów sesji.

## Wymagania

- Android 13 lub nowszy.
- Urządzenie ARM64; zgodność GPU, Vulkan i gry zależy od konkretnego modelu.
- Winlator zainstalowany osobno dla trybu lokalnego.
- Komputer z uruchomionym Steam oraz Steam Link na telefonie dla trybu zdalnego.
- JDK 17 i Android SDK 35 do kompilacji.

## Budowanie APK

Otwórz katalog repozytorium w Android Studio, poczekaj na synchronizację Gradle, a następnie wybierz **Build → Build APK(s)**.

Konfiguracja projektu używa Android Gradle Plugin 8.7.3, Gradle 8.9, Kotlin 2.0.21 i SDK 35. GitHub Actions buduje app-debug.apk po każdym pushu do gałęzi main i publikuje APK jako artefakt workflow.

## Instalowanie gry lokalnie

1. Zainstaluj Winlator z oficjalnego źródła i utwórz kontener Windows.
2. Zainstaluj Steam wewnątrz kontenera Winlator, korzystając z oficjalnego instalatora.
3. Zaloguj się wyłącznie w oficjalnym kliencie Steam uruchomionym w kontenerze.
4. W STEMAndroid dodaj grę i jej Steam App ID.
5. Wybierz **BAT instalacji** i zapisz plik np. w folderze Download dostępnym z kontenera.
6. Otwórz zapisany plik BAT w Winlatorze. Steam powinien obsłużyć żądanie instalacji, jeżeli protokół Steam URI jest poprawnie zarejestrowany.
7. Po pobraniu gry wypełnij ścieżkę jej EXE w kontenerze. Do uruchomienia przez Steam wygeneruj **BAT uruchomienia** i wykonaj go w kontenerze.

W razie problemów uruchom instalację lub grę bezpośrednio z biblioteki Steam wewnątrz Winlatora. Wymagane jest posiadanie gry na koncie i wystarczająca ilość miejsca.

## Licencje i niezależność

STEMAndroid jest projektem fanowskim, niezwiązanym z Valve. Nie zawiera plików Steam, Wine ani Winlator. Użytkownik instaluje wymagane komponenty oddzielnie i korzysta z własnego konta oraz legalnie posiadanych gier. Znaki towarowe należą do ich właścicieli.

## Plan rozwoju

Zobacz docs/ROADMAP.md i docs/ARCHITECTURE.md.
