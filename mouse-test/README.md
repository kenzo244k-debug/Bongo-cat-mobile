# Bongo Cat Mouse Test — Floating Cat

Aplicativo experimental para testar se um serviço de acessibilidade Android consegue receber eventos de mouse enquanto outro app está em primeiro plano.

## Como testar
1. Instale o APK.
2. Conceda "Permitir sobreposição" para mostrar a bolinha.
3. Ative "Bongo Cat — teste do mouse" em Configurações > Acessibilidade.
4. Volte ao app e confirme que a bolinha com o gato aparece.
5. Abra outro app de teste e mova o mouse. O gato deve atualizar o texto para MOVE, CLICK ou RODÍZIO se o Android entregar esses eventos ao serviço.

## Aviso essencial
Este método usa AccessibilityService.setMotionEventSources (Android 14+). A entrega de eventos de movimento ao serviço pode interferir no encaminhamento desses eventos para outros apps/aparelhos. Portanto, o teste pode fazer o mouse deixar de controlar corretamente o app em primeiro plano. Não use durante partidas. Desative o serviço em Configurações > Acessibilidade para voltar ao comportamento normal.

O app não registra texto digitado, não lê conteúdo de telas, não injeta cliques e não envia dados para a internet. O overlay só exibe o tipo e a contagem dos eventos recebidos.

Build: GitHub Actions, workflow "Build Bongo Cat Mouse Test APK".
