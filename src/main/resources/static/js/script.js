// Executa o código quando o documento estiver pronto
$(function () {
  
  // --- LÓGICA PARA O FORMULÁRIO DE LOCADORES (ADICIONAR/REMOVER CONTATOS) ---
  const landlordFormElements = {
    container: $('#contacts-container'),
    addBtn: $('#add-contact-btn'),
    template: $('#contact-template')
  };
  if (landlordFormElements.container.length > 0) {
    let contactIndex = landlordFormElements.container.find('.contact-row').length;
    landlordFormElements.addBtn.on('click', function () {
      const templateElement = landlordFormElements.template[0];
      let templateHtml = templateElement.innerHTML;
      let newRowHtml = templateHtml.replace(/INDEX/g, contactIndex);
      landlordFormElements.container.append(newRowHtml);
      contactIndex++;
    });
    landlordFormElements.container.on('click', '.remove-contact-btn', function () {
      $(this).closest('.contact-row').remove();
    });
  }


  // --- LÓGICA PARA A LISTA DE IMÓVEIS (SELEÇÃO E DATAS) ---
  const propertyListElements = {
    checkAll: $("#checkAllTable"),
    checkboxes: $(".property-checkbox:not(:disabled)"),
    submitBtn: $("#generateReceiptsBtn"),
    tableRows: $("tbody tr"),
    monthSelect: $("#month"),
    yearSelect: $("#year")
  };
  if (propertyListElements.checkAll.length > 0) {
    function updateRowHighlights() {
      propertyListElements.tableRows.removeClass("table-active");
      propertyListElements.checkboxes
        .filter(":checked")
        .closest("tr")
        .addClass("table-active");
    }
    function toggleSubmitButtonState() {
      const anyChecked = propertyListElements.checkboxes.filter(":checked").length > 0;
      propertyListElements.submitBtn.prop("disabled", !anyChecked);
    }
    propertyListElements.checkAll.on("click", function () {
      propertyListElements.checkboxes.prop("checked", $(this).is(":checked"));
      updateRowHighlights();
      toggleSubmitButtonState();
    });
    propertyListElements.checkboxes.on("change", function () {
      if (propertyListElements.checkboxes.length === propertyListElements.checkboxes.filter(":checked").length) {
        propertyListElements.checkAll.prop("checked", true).prop("indeterminate", false);
      } else if (propertyListElements.checkboxes.filter(":checked").length > 0) {
        propertyListElements.checkAll.prop("checked", false).prop("indeterminate", true);
      } else {
        propertyListElements.checkAll.prop("checked", false).prop("indeterminate", false);
      }
      updateRowHighlights();
      toggleSubmitButtonState();
    });
    updateRowHighlights();
    toggleSubmitButtonState();
    const now = new Date();
    const currentMonthIndex = now.getMonth();
    const currentYear = now.getFullYear();
    const monthNames = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
    propertyListElements.monthSelect.val(monthNames[currentMonthIndex]);
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

  
  // --- LÓGICA PARA O FORMULÁRIO DE ENERGIA (DATA PADRÃO) ---
  const energyDateField = $('#date');
  if (energyDateField.length > 0) {
    if (!energyDateField.val()) {
      const today = new Date();
      const formattedDate = today.toISOString().split('T')[0];
      energyDateField.val(formattedDate);
    }
  }
  
  
  // --- LÓGICA PARA A PÁGINA DE RECIBOS (VALOR POR EXTENSO) ---
  if ($('#print-area').length > 0) {
    function numeroParaExtenso(valor) {
      valor = parseFloat(valor).toFixed(2);
      let numero = valor.split('.')[0];
      let centavos = valor.split('.')[1];
      const unidades = ["", "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove"];
      const especiais = ["dez", "onze", "doze", "treze", "catorze", "quinze", "dezesseis", "dezessete", "dezoito", "dezenove"];
      const dezenas = ["", "", "vinte", "trinta", "quarenta", "cinquenta", "sessenta", "setenta", "oitenta", "noventa"];
      const centenas = ["", "cento", "duzentos", "trezentos", "quatrocentos", "quinhentos", "seiscentos", "setecentos", "oitocentos", "novecentos"];
      function getExtenso(n) {
        if (n.length === 0) return "";
        let num = parseInt(n, 10);
        if (num === 0) return "";
        if (num === 100) return "cem";
        let str = "";
        let c = Math.floor(num / 100);
        let d = Math.floor((num % 100) / 10);
        let u = num % 10;
        if (c > 0) str += centenas[c] + (num % 100 !== 0 ? " e " : "");
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
    $('.value-in-words').each(function() {
      const valorNumerico = $(this).data('value');
      if (valorNumerico !== undefined) {
        const textoExtenso = numeroParaExtenso(valorNumerico);
        $(this).text(textoExtenso);
      }
    });
  }


  // --- LÓGICA PARA O FORMULÁRIO DE CONTRATO (DATAS) ---
  if ($('#startMonth').length > 0) {
    const startMonthSelect = $('#startMonth');
    const startYearSelect = $('#startYear');
    const endMonthSelect = $('#endMonth');
    const endYearSelect = $('#endYear');

    const now = new Date();
    const currentMonthIndex = now.getMonth();
    const currentYear = now.getFullYear();

    // 1. Popula os seletores de Mês
    const monthNames = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
    monthNames.forEach((month, index) => {
      const option = $('<option></option>').val(index + 1).text(month);
      startMonthSelect.append(option.clone());
      endMonthSelect.append(option.clone());
    });
    
    // Define o valor padrão APÓS popular
    startMonthSelect.val(currentMonthIndex + 1);
    endMonthSelect.val(currentMonthIndex + 1);

    // 2. Popula os seletores de Ano
    const startYearRange = currentYear - 1;
    const endYearRange = currentYear + 5;
    for (let year = startYearRange; year <= endYearRange; year++) {
      const option = $('<option></option>').val(year).text(year);
      startYearSelect.append(option.clone());
      endYearSelect.append(option.clone());
    }
    
    // Define o valor padrão APÓS popular
    startYearSelect.val(currentYear);
    endYearSelect.val(currentYear);
  }

  // --- NOVO: LÓGICA PARA MÁSCARA E VALIDAÇÃO DE CPF/CNPJ ---
  const cpfCnpjField = $('#cpfCnpj');

  if (cpfCnpjField.length > 0) {
    const validaCPF = (cpf) => {
      cpf = cpf.replace(/[^\d]+/g, '');
      if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) return false;
      let soma = 0, resto;
      for (let i = 1; i <= 9; i++) soma += parseInt(cpf.substring(i - 1, i)) * (11 - i);
      resto = (soma * 10) % 11;
      if ((resto === 10) || (resto === 11)) resto = 0;
      if (resto !== parseInt(cpf.substring(9, 10))) return false;
      soma = 0;
      for (let i = 1; i <= 10; i++) soma += parseInt(cpf.substring(i - 1, i)) * (12 - i);
      resto = (soma * 10) % 11;
      if ((resto === 10) || (resto === 11)) resto = 0;
      if (resto !== parseInt(cpf.substring(10, 11))) return false;
      return true;
    };

    const validaCNPJ = (cnpj) => {
      cnpj = cnpj.replace(/[^\d]+/g, '');
      if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) return false;
      let tamanho = cnpj.length - 2;
      let numeros = cnpj.substring(0, tamanho);
      let digitos = cnpj.substring(tamanho);
      let soma = 0, pos = tamanho - 7;
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
      for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
      }
      resultado = soma % 11 < 2 ? 0 : 11 - (soma % 11);
      if (resultado != digitos.charAt(1)) return false;
      return true;
    };

    const handleCpfCnpjInput = (event) => {
      const input = event.target;
      const feedback = $('#cpfCnpjFeedback');
      let value = input.value.replace(/\D/g, '');

      if (value.length > 11) {
        value = value.replace(/^(\d{2})(\d)/, '$1.$2');
        value = value.replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3');
        value = value.replace(/\.(\d{3})(\d)/, '.$1/$2');
        value = value.replace(/(\d{4})(\d)/, '$1-$2');
        input.value = value.substring(0, 18);
      } else {
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        input.value = value.substring(0, 14);
      }

      const rawValue = input.value.replace(/\D/g, '');
      if (rawValue.length === 11) {
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
        input.classList.remove('is-valid');
        input.classList.add('is-invalid');
        feedback.text('CPF ou CNPJ incompleto.');
      } else {
        input.classList.remove('is-valid', 'is-invalid');
        feedback.text('');
      }
    };
    
    cpfCnpjField.on('input', handleCpfCnpjInput);
  }

});