/**
 * ============================================================================
 * contract-editor.js - Editor de Contratos com Formatação de Texto
 *
 * Fornece funcionalidades de formatação de texto para o editor de contratos,
 * incluindo negrito, itálico e sublinhado.
 * ============================================================================
 */

document.addEventListener('DOMContentLoaded', function () {
  // Elementos do editor
  const boldButton = document.getElementById('boldButton');
  const italicButton = document.getElementById('italicButton');
  const underlineButton = document.getElementById('underlineButton');

  /**
   * Aplica um comando de formatação ao texto selecionado
   * @param {string} command - Comando de formatação (bold, italic, underline)
   */
  const applyStyle = (command) => {
    document.execCommand(command, false, null);
  };

  /**
   * Previne a perda de foco do editor ao clicar nos botões
   * @param {MouseEvent} e - Evento de mousedown
   * @param {string} command - Comando de formatação a ser aplicado
   */
  const handleButtonClick = (e, command) => {
    e.preventDefault(); // Previne que o botão tome o foco
    applyStyle(command);
  };

  // Registrar event listeners para os botões de formatação
  // Usamos mousedown ao invés de click para prevenir perda de foco do editor

  boldButton.addEventListener('mousedown', (e) => {
    handleButtonClick(e, 'bold');
  });

  italicButton.addEventListener('mousedown', (e) => {
    handleButtonClick(e, 'italic');
  });

  underlineButton.addEventListener('mousedown', (e) => {
    handleButtonClick(e, 'underline');
  });
});
