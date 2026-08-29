[README.md](https://github.com/user-attachments/files/31604049/README.md)
# PataCerta — Aplicativo Mobile

Aplicativo Android (Java) para gestão de saúde e bem-estar de pets — vacinas,
lembretes, peso e localização de clínicas veterinárias. Desenvolvido como
continuidade da **Atividade Avaliativa 1** (planejamento/protótipo) para a
**Atividade Avaliativa 2** (implementação).

> Para o relatório técnico completo (arquitetura, decisões de design,
> integrações de API e testes), veja `RELATORIO_TECNICO.pdf` / `.docx` na
> raiz do repositório.

## Stack técnica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 11 |
| Build | Gradle 8.7 (Android Gradle Plugin 8.4.1) |
| UI | Android Views + Material Components + ViewBinding |
| Persistência local | Room (SQLite) |
| Rede | Retrofit2 + OkHttp + Gson |
| Lembretes locais | AlarmManager + NotificationCompat |
| Imagens | Glide |

## APIs integradas

1. **[reqres.in](https://reqres.in)** — autenticação (login/cadastro) de demonstração.
   Credencial de teste pré-preenchida na tela de login: `eve.holt@reqres.in` / `cityslicka`.
2. **[TheDogAPI](https://www.thedogapi.com)** — autocomplete de raças ao cadastrar um cão.
   Requer uma chave gratuita própria — troque `DOG_API_KEY` em
   `ApiClient.java` pela sua chave (a chave incluída é apenas um placeholder).
3. **[Nominatim / OpenStreetMap](https://nominatim.org)** — busca de clínicas
   veterinárias por texto (sem necessidade de chave paga do Google Places).

## Como abrir e rodar

1. Instale o **Android Studio** (Koala ou mais recente) com Android SDK 34.
2. `File > Open` e selecione a pasta raiz deste projeto.
3. Aguarde o Gradle sincronizar (vai baixar as dependências listadas em `app/build.gradle`).
4. Rode em um emulador ou dispositivo físico com Android 9 (API 28) ou superior.
5. Na tela de login, use a credencial de teste já preenchida, ou toque em
   "Cadastre-se gratuitamente" para simular um novo cadastro via reqres.in.

> **Nota:** este projeto foi escrito e revisado neste ambiente sem acesso a um
> Android SDK/emulador real (sandbox sem rede/Android Studio instalado), então
> ele não foi compilado neste momento. O código segue as APIs estáveis do
> Android/AndroidX na versão indicada e foi revisado com cuidado, mas
> recomenda-se rodar `./gradlew assembleDebug` (ou usar o próprio Android
> Studio) antes da entrega final para capturar eventuais ajustes de sintaxe
> ou de versão de dependência. Veja a seção "Limitações conhecidas" no
> relatório técnico.

## Estrutura de pastas

```
app/src/main/java/com/patacerta/app/
├── data/
│   ├── local/          # Room: entidades, DAOs, AppDatabase
│   ├── remote/          # Retrofit: serviços e modelos das 3 APIs
│   └── repository/      # PetRepository, AuthRepository
├── notification/         # AlarmManager, NotificationCompat, BroadcastReceiver
├── ui/
│   ├── onboarding/
│   ├── auth/             # Login, Cadastro
│   ├── home/              # Dashboard
│   ├── petprofile/        # Perfil do pet, cadastro de pet, gráfico de peso
│   ├── reminder/           # Novo lembrete
│   ├── locator/            # Localizador de clínicas
│   └── notifications/      # Central de notificações
└── util/
```

## Publicando no GitHub

```bash
cd PataCerta
git init
git add .
git commit -m "Implementação inicial do PataCerta (Atividade Avaliativa 2)"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/patacerta-app.git
git push -u origin main
```

## Licença

Projeto acadêmico, desenvolvido para fins educacionais.
