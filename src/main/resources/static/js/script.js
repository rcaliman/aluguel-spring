/**
 * ============================================================================
 * script.js - JavaScript Principal do Sistema de Aluguel de Imóveis
 *
 * Contém toda a lógica do frontend para:
 * - Formulário de Locadores (adicionar/remover contatos)
 * - Lista de Imóveis (seleção e geração de recibos)
 * - Formulário de Energia (data padrão)
 * - Página de Recibos (conversão de valores para extenso)
 * - Formulário de Contrato (seleção de datas)
 * - Validação de CPF/CNPJ com máscara
 * ============================================================================
 */

$(function () {

  // ========================================================================
  // 1. FORMULÁRIO DE LOCADORES - Gestão de Contatos Dinâmicos
  // ========================================================================

  const landlordFormElements = {
    container: $('#contacts-container'),
    addBtn: $('#add-contact-btn'),
    template: $('#contact-template')
  };

  if (landlordFormElements.container.length > 0) {
    let contactIndex = landlordFormElements.container.find('.contact-row').length;

    // Adicionar novo contato
    landlordFormElements.addBtn.on('click', function () {
      const templateElement = landlordFormElements.template[0];
      let templateHtml = templateElement.innerHTML;
      let newRowHtml = templateHtml.replace(/INDEX/g, contactIndex);
      landlordFormElements.container.append(newRowHtml);
      contactIndex++;
    });

    // Remover contato existente
    landlordFormElements.container.on('click', '.remove-contact-btn', function () {
      $(this).closest('.contact-row').remove();
    });
  }

  // ========================================================================
  // 2. LISTA DE IMÓVEIS - Seleção e Geração de Recibos
  // ========================================================================

  const propertyListElements = {
    checkAll: $("#checkAllTable"),
    checkboxes: $(".property-checkbox:not(:disabled)"),
    submitBtn: $("#generateReceiptsBtn"),
    tableRows: $("tbody tr"),
    monthSelect: $("#month"),
    yearSelect: $("#year")
  };

  if (propertyListElements.checkAll.length > 0) {

    /**
     * Atualiza o highlight visual das linhas selecionadas na tabela
     */
    function updateRowHighlights() {
      propertyListElements.tableRows.removeClass("table-active");
      propertyListElements.checkboxes
        .filter(":checked")
        .closest("tr")
        .addClass("table-active");
    }

    /**
     * Habilita/desabilita o botão de gerar recibos baseado na seleção
     */
    function toggleSubmitButtonState() {
      const anyChecked = propertyListElements.checkboxes.filter(":checked").length > 0;
      propertyListElements.submitBtn.prop("disabled", !anyChecked);
    }

    // Checkbox "Selecionar Todos"
    propertyListElements.checkAll.on("click", function () {
      propertyListElements.checkboxes.prop("checked", $(this).is(":checked"));
      updateRowHighlights();
      toggleSubmitButtonState();
    });

    // Checkboxes individuais
    propertyListElements.checkboxes.on("change", function () {
      const totalCheckboxes = propertyListElements.checkboxes.length;
      const checkedCount = propertyListElements.checkboxes.filter(":checked").length;

      if (totalCheckboxes === checkedCount) {
        // Todos selecionados
        propertyListElements.checkAll.prop("checked", true).prop("indeterminate", false);
      } else if (checkedCount > 0) {
        // Alguns selecionados
        propertyListElements.checkAll.prop("checked", false).prop("indeterminate", true);
      } else {
        // Nenhum selecionado
        propertyListElements.checkAll.prop("checked", false).prop("indeterminate", false);
      }

      updateRowHighlights();
      toggleSubmitButtonState();
    });

    // Inicializar estados
    updateRowHighlights();
    toggleSubmitButtonState();

    // Configurar seletor de mês/ano com valores atuais
    const now = new Date();
    const currentMonthIndex = now.getMonth();
    const currentYear = now.getFullYear();
    const monthNames = [
      "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
      "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    ];

    // Definir mês atual
    propertyListElements.monthSelect.val(monthNames[currentMonthIndex]);

    // Preencher anos (ano anterior, atual e próximo)
    const yearSelect = propertyListElements.yearSelect;
    const years = [currentYear - 1, currentYear, currentYear + 1];
    yearSelect.empty();

    years.forEach(year => {
      const option = $('<option></option>').val(year).text(year);
      if (year === currentYear) {
        option.prop('selected', true);
      }
      yearSelect.append(option);
    });
  }

  // ========================================================================
  // 3. FORMULÁRIO DE ENERGIA - Data Padrão
  // ========================================================================

  const energyDateField = $('#date');

  if (energyDateField.length > 0) {
    // Define a data atual se o campo estiver vazio
    if (!energyDateField.val()) {
      const today = new Date();
      const formattedDate = today.toISOString().split('T')[0];
      energyDateField.val(formattedDate);
    }
  }

  // ========================================================================
  // 4. PÁGINA DE RECIBOS - Conversão de Valores para Extenso
  // ========================================================================

  if ($('#print-area').length > 0) {

    /**
     * Converte um valor numérico para sua representação por extenso
     * @param {number} valor - Valor a ser convertido
     * @returns {string} - Valor por extenso
     */
    function numeroParaExtenso(valor) {
      valor = parseFloat(valor).toFixed(2);
      let numero = valor.split('.')[0];
      let centavos = valor.split('.')[1];

      const unidades = ["", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove"];
      const especiais = ["dez", "onze", "doze", "treze", "catorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"];
      const dezenas = ["", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"];
      const centenas = ["", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"];

      /**
       * Converte até 3 dígitos para extenso
       * @param {string} n - Número com até 3 dígitos
       * @returns {string} - Número por extenso
       */
      function getExtenso(n) {
        if (n.length === 0) return "";

        let num = parseInt(n, 10);
        if (num === 0) return "";
        if (num === 100) return "cem";

        let str = "";
        let c = Math.floor(num / 100);
        let d = Math.floor((num % 100) / 10);
        let u = num % 10;

        if (c > 0) {
          str += centenas[c] + (num % 100 !== 0 ? " e " : "");
        }

        if (d === 1) {
          str += especiais[u];
        } else {
          if (d > 1) str += dezenas[d] + (u !== 0 ? " e " : "");
          if (u > 0 && d !== 1) str += unidades[u];
        }

        return str;
      }

      let parteMilhar = getExtenso(numero.slice(0, -3));
      let parteCentena = getExtenso(numero.slice(-3));
      let extenso = "";

      if (parteMilhar) {
        extenso += (parseInt(numero.slice(0, -3), 10) === 1 ? "mil" : parteMilhar + " mil");
        if (parteCentena) {
          if (parteCentena.startsWith(" e ") || parseInt(numero.slice(-3), 10) === 100) {
            extenso += " ";
          } else {
            extenso += " e ";
          }
        }
      }

      if (parteCentena) extenso += parteCentena;
      if (!extenso) extenso = "zero";

      let reais = parseInt(numero, 10);
      extenso += (reais === 1 ? " real" : " reais");

      if (parseInt(centavos, 10) > 0) {
        let centavosExtenso = getExtenso(centavos);
        extenso += " e " + centavosExtenso + (parseInt(centavos, 10) === 1 ? " centavo" : " centavos");
      }

      return extenso;
    }

    // Aplicar conversão a todos os elementos com a classe 'value-in-words'
    $('.value-in-words').each(function() {
      const valorNumerico = $(this).data('value');
      if (valorNumerico !== undefined) {
        const textoExtenso = numeroParaExtenso(valorNumerico);
        $(this).text(textoExtenso);
      }
    });
  }

  // ========================================================================
  // 5. FORMULÁRIO DE CONTRATO - Seleção de Datas (Mês/Ano)
  // ========================================================================

  if ($('#startMonth').length > 0) {
    const startMonthSelect = $('#startMonth');
    const startYearSelect = $('#startYear');
    const endMonthSelect = $('#endMonth');
    const endYearSelect = $('#endYear');

    const now = new Date();
    const currentMonthIndex = now.getMonth();
    const currentYear = now.getFullYear();

    // Preencher seletores de mês
    const monthNames = [
      "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
      "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    ];

    monthNames.forEach((month, index) => {
      const option = $('<option></option>').val(index + 1).text(month);
      startMonthSelect.append(option.clone());
      endMonthSelect.append(option.clone());
    });

    // Definir mês atual como padrão
    startMonthSelect.val(currentMonthIndex + 1);
    endMonthSelect.val(currentMonthIndex + 1);

    // Preencher seletores de ano
    const startYearRange = currentYear - 1;
    const endYearRange = currentYear + 5;

    for (let year = startYearRange; year <= endYearRange; year++) {
      const option = $('<option></option>').val(year).text(year);
      startYearSelect.append(option.clone());
      endYearSelect.append(option.clone());
    }

    // Definir ano atual como padrão
    startYearSelect.val(currentYear);
    endYearSelect.val(currentYear);
  }

  // ========================================================================
  // 6. VALIDAÇÃO E MÁSCARA DE CPF/CNPJ
  // ========================================================================

  const cpfCnpjField = $('#cpfCnpj');

  if (cpfCnpjField.length > 0) {

    /**
     * Valida um CPF
     * @param {string} cpf - CPF a ser validado
     * @returns {boolean} - True se válido, false caso contrário
     */
    const validaCPF = (cpf) => {
      cpf = cpf.replace(/[^\d]+/g, '');

      if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;

      let soma = 0, resto;

      // Primeiro dígito verificador
      for (let i = 1; i <= 9; i++) {
        soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
      }
      resto = (soma * 10) % 11;
      if ((resto === 10) || (resto === 11)) resto = 0;
      if (resto !== parseInt(cpf.substring(9, 10))) return false;

      soma = 0;

      // Segundo dígito verificador
      for (let i = 1; i <= 10; i++) {
        soma += parseInt(cpf.substring(i - 1, i)) * (12 - i);
      }
      resto = (soma * 10) % 11;
      if ((resto === 10) || (resto === 11)) resto = 0;
      if (resto !== parseInt(cpf.substring(10, 11))) return false;

      return true;
    };

    /**
     * Valida um CNPJ
     * @param {string} cnpj - CNPJ a ser validado
     * @returns {boolean} - True se válido, false caso contrário
     */
    const validaCNPJ = (cnpj) => {
      cnpj = cnpj.replace(/[^\d]+/g, '');

      if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) return false;

      let tamanho = cnpj.length - 2;
      let numeros = cnpj.substring(0, tamanho);
      let digitos = cnpj.substring(tamanho);
      let soma = 0, pos = tamanho - 7;

      // Primeiro dígito verificador
      for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
      }
      let resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
      if (resultado != digitos.charAt(0)) return false;

      tamanho = tamanho + 1;
      numeros = cnpj.substring(0, tamanho);
      soma = 0;
      pos = tamanho - 7;

      // Segundo dígito verificador
      for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
      }
      resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
      if (resultado != digitos.charAt(1)) return false;

      return true;
    };

    /**
     * Gerencia a entrada de CPF/CNPJ: máscara e validação
     * @param {Event} event - Evento de input
     */
    const handleCpfCnpjInput = (event) => {
      const input = event.target;
      const feedback = $('#cpfCnpjFeedback');
      let value = input.value.replace(/\D/g, '');

      // Aplicar máscara baseada no tamanho
      if (value.length > 11) {
        // Máscara CNPJ: XX.XXX.XXX/XXXX-XX
        value = value.replace(/^(\d{2})(\d)/, '$1.$2');
        value = value.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
        value = value.replace(/\.(\d{3})(\d)/, '.$1/$2');
        value = value.replace(/(\d{4})(\d)/, '$1-$2');
        input.value = value.substring(0, 18);
      } else {
        // Máscara CPF: XXX.XXX.XXX-XX
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        input.value = value.substring(0, 14);
      }

      // Validação em tempo real
      const rawValue = input.value.replace(/\D/g, '');

      if (rawValue.length === 11) {
        // Validar CPF
        if (validaCPF(rawValue)) {
          input.classList.remove('is-invalid');
          input.classList.add('is-valid');
          feedback.text('');
        } else {
          input.classList.remove('is-valid');
          input.classList.add('is-invalid');
          feedback.text('CPF inválido. Verifique o número digitado.');
        }
      } else if (rawValue.length === 14) {
        // Validar CNPJ
        if (validaCNPJ(rawValue)) {
          input.classList.remove('is-invalid');
          input.classList.add('is-valid');
          feedback.text('');
        } else {
          input.classList.remove('is-valid');
          input.classList.add('is-invalid');
          feedback.text('CNPJ inválido. Verifique o número digitado.');
        }
      } else if (rawValue.length > 0) {
        // Documento incompleto
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
        feedback.text('CPF ou CNPJ incompleto.');
      } else {
        // Campo vazio
        input.classList.remove('is-valid', 'is-invalid');
        feedback.text('');
      }
    };

    // Registrar o handler de input
    cpfCnpjField.on('input', handleCpfCnpjInput);
  }

  // ========================================================================
  // 7. FORMATAÇÃO DE CPF/CNPJ EM LISTAGENS
  // ========================================================================

  /**
   * Formata um CPF/CNPJ para exibição
   * @param {string} value - CPF/CNPJ sem formatação
   * @returns {string} - CPF/CNPJ formatado
   */
  function formatCpfCnpj(value) {
    if (!value) return '';

    const cleanValue = value.replace(/\D/g, '');

    if (cleanValue.length === 11) {
      // Formatar como CPF: XXX.XXX.XXX-XX
      return cleanValue.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    } else if (cleanValue.length === 14) {
      // Formatar como CNPJ: XX.XXX.XXX/XXXX-XX
      return cleanValue.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    }

    return value; // Retorna o valor original se não for CPF nem CNPJ
  }

  // Aplicar formatação a todos os elementos com classe cpf-cnpj-display
  $('.cpf-cnpj-display').each(function() {
    const $element = $(this);

    // Verificar se é um input ou elemento de texto
    if ($element.is('input')) {
      const rawValue = $element.val();
      const formatted = formatCpfCnpj(rawValue);
      $element.val(formatted);
    } else {
      const rawValue = $element.text().trim();
      const formatted = formatCpfCnpj(rawValue);
      $element.text(formatted);
    }
  });

});
