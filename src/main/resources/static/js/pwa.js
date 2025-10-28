// Registro do Service Worker para PWA
if ('serviceWorker' in navigator) {
  window.addEventListener('load', () => {
    navigator.serviceWorker.register('/sw.js')
      .then(registration => {
        console.log('[PWA] Service Worker registrado com sucesso:', registration.scope);

        // Verifica se há uma atualização disponível
        registration.addEventListener('updatefound', () => {
          const newWorker = registration.installing;
          newWorker.addEventListener('statechange', () => {
            if (newWorker.state === 'installed' && navigator.serviceWorker.controller) {
              console.log('[PWA] Nova versão disponível!');
              // Opcional: exibir mensagem para o usuário recarregar a página
            }
          });
        });
      })
      .catch(error => {
        console.error('[PWA] Erro ao registrar Service Worker:', error);
      });
  });
}

// Detecta quando o app é instalado
window.addEventListener('beforeinstallprompt', event => {
  console.log('[PWA] beforeinstallprompt disparado');
  // Previne o prompt automático do navegador
  event.preventDefault();
  // Armazena o evento para uso posterior
  window.deferredPrompt = event;

  // Opcional: exibir um botão customizado de instalação
  // showInstallButton();
});

// Detecta quando o app foi instalado
window.addEventListener('appinstalled', event => {
  console.log('[PWA] App instalado com sucesso!');
  window.deferredPrompt = null;
});

// Função opcional para mostrar prompt de instalação customizado
function showInstallPrompt() {
  if (window.deferredPrompt) {
    window.deferredPrompt.prompt();
    window.deferredPrompt.userChoice.then(choiceResult => {
      if (choiceResult.outcome === 'accepted') {
        console.log('[PWA] Usuário aceitou a instalação');
      } else {
        console.log('[PWA] Usuário recusou a instalação');
      }
      window.deferredPrompt = null;
    });
  }
}
