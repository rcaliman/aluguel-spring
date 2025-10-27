/**
 * ============================================================================
 * energy-list.js - Scripts para a página de leituras de energia
 * ============================================================================
 */

document.addEventListener("DOMContentLoaded", function () {
  // Lógica do botão de imprimir
  const printButton = document.getElementById("printButton");
  if (printButton) {
    printButton.addEventListener("click", function () {
      window.print();
    });
  }
});
