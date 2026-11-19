# Roadmap

## 0.1.0 — MVP (obecny etap)
- [x] Projekt Android/Kotlin/Compose.
- [x] Minimalny Android 13 (API 33), target SDK 35.
- [x] Lokalna biblioteka: dodawanie i usuwanie pozycji.
- [x] Zapis nazwy, Steam App ID i ścieżki EXE.
- [x] Otwarcie strony Steam Store.
- [x] Otwarcie Winlatora i kopiowanie ścieżki EXE.
- [x] Otwieranie Steam Link.
- [x] Workflow budowania APK w GitHub Actions.

## 0.2.0 — Runner Bridge
- [ ] Zweryfikować publiczny interfejs uruchamiania aplikacji w wybranym runtime.
- [ ] Dodać adapter z kontrolą wersji runtime.
- [ ] Uruchamiać wybraną grę bez ręcznego wpisywania ścieżki, o ile runtime udostępni bezpieczny interfejs.
- [ ] Dodać profile rozdzielczości, sterowania i ustawień per gra.

## 0.3.0 — Steam Container Workflow
- [ ] Udokumentować powtarzalny proces instalacji Steam PC w kontenerze.
- [ ] Dodać wykrywanie biblioteki Steam i plików manifestów bez obchodzenia DRM.
- [ ] Dodać walidację miejsca na dane i kontrolę integralności plików.
- [ ] Nie przechowywać poświadczeń Steam w STEMAndroid.

## 0.4.0 — Game Compatibility
- [ ] Profile zgodności DXVK/VKD3D/Turnip zależne od runtime.
- [ ] Baza testów zgodności per gra i urządzenie.
- [ ] Diagnostyka Vulkan, RAM, pamięci masowej i architektury CPU.
- [ ] Eksport raportu diagnostycznego.

## 1.0.0 — Release
- [ ] Stabilne API runnera i testy integracyjne.
- [ ] Migracje danych biblioteki.
- [ ] Testy na wielu urządzeniach Android 13+.
- [ ] Podpisany release APK i instrukcja instalacji.
