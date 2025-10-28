// Service Worker para PWA - Aluguel de Imóveis
const CACHE_NAME = 'aluguel-imoveis-v1';
const urlsToCache = [
  '/',
  '/css/style.css',
  '/js/script.js',
  '/webjars/bootstrap/css/bootstrap.min.css',
  '/webjars/bootstrap-icons/font/bootstrap-icons.css',
  '/webjars/bootstrap/5.3.3/js/bootstrap.bundle.min.js',
  '/webjars/jquery/3.7.1/jquery.min.js',
  '/webjars/jquery-mask-plugin/1.14.16/dist/jquery.mask.min.js'
];

// Instalação do Service Worker
self.addEventListener('install', event => {
  console.log('[Service Worker] Instalando...');
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('[Service Worker] Abrindo cache');
        return cache.addAll(urlsToCache);
      })
      .catch(err => {
        console.log('[Service Worker] Erro ao fazer cache:', err);
      })
  );
  // Força o service worker a se tornar ativo imediatamente
  self.skipWaiting();
});

// Ativação do Service Worker
self.addEventListener('activate', event => {
  console.log('[Service Worker] Ativando...');
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (cacheName !== CACHE_NAME) {
            console.log('[Service Worker] Removendo cache antigo:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
  // Assume o controle de todas as páginas imediatamente
  return self.clients.claim();
});

// Interceptação de requisições (estratégia Network First com fallback para Cache)
self.addEventListener('fetch', event => {
  // Ignora requisições que não sejam GET
  if (event.request.method !== 'GET') {
    return;
  }

  // Ignora requisições de logout e CSRF
  if (event.request.url.includes('/logout') ||
      event.request.url.includes('/csrf')) {
    return;
  }

  event.respondWith(
    fetch(event.request)
      .then(response => {
        // Se conseguiu da rede, atualiza o cache
        if (response.status === 200) {
          const responseToCache = response.clone();
          caches.open(CACHE_NAME)
            .then(cache => {
              cache.put(event.request, responseToCache);
            });
        }
        return response;
      })
      .catch(() => {
        // Se falhou na rede, tenta buscar do cache
        return caches.match(event.request)
          .then(response => {
            if (response) {
              return response;
            }
            // Se não está no cache, retorna uma resposta de fallback para navegação
            if (event.request.mode === 'navigate') {
              return caches.match('/');
            }
          });
      })
  );
});

// Mensagens do cliente
self.addEventListener('message', event => {
  if (event.data && event.data.type === 'SKIP_WAITING') {
    self.skipWaiting();
  }
});
