# Migrações do Banco de Dados com Flyway

## Estrutura Atual

O banco de dados foi inicialmente criado com Hibernate DDL (`ddl-auto=update`) e posteriormente migrado para Flyway.

### Baseline (V1)

- **Versão**: 1
- **Descrição**: Estado inicial do banco de dados (schema existente antes do Flyway)
- O Flyway foi configurado com `baseline-on-migrate=true` e `baseline-version=1`
- Isso significa que o Flyway marcou o estado atual do banco como V1 sem executar nenhuma migration

## Criando Novas Migrações

### Convenção de Nomenclatura

As novas migrações devem seguir o padrão:

```
V<versão>__<descrição>.sql
```

Exemplos:

- `V2__add_new_column_to_property.sql`
- `V3__create_payment_table.sql`
- `V4__add_index_to_tenant_cpf.sql`

### Regras Importantes

1. **Versão**: Deve começar em V2 (V1 é o baseline)
2. **Separador**: Usar **dois underscores** (`__`) entre versão e descrição
3. **Descrição**: Usar snake_case, ser descritiva e clara
4. **Conteúdo**: SQL puro, sem comentários desnecessários

### Exemplo de Migration

```sql
-- V2__add_energy_reading_index.sql

CREATE INDEX idx_energy_reading_date
ON energy_readings(reading_date);

ALTER TABLE properties
ADD COLUMN last_maintenance_date DATE NULL;
```

## Processo de Deploy

### Desenvolvimento

1. Criar o arquivo de migration em `src/main/resources/db/migration/`
2. Reiniciar a aplicação
3. O Flyway executará automaticamente as novas migrations
4. Verificar a tabela `flyway_schema_history` para confirmar

### Produção

1. Fazer backup do banco de dados
2. Copiar o arquivo `application.properties-prd` para `application.properties`
3. Iniciar a aplicação
4. O Flyway executará as migrations pendentes automaticamente
5. Verificar logs e tabela `flyway_schema_history`

## Comandos Úteis

### Verificar status das migrations

```bash
mvn flyway:info
```

### Aplicar migrations manualmente

```bash
mvn flyway:migrate
```

### Validar migrations

```bash
mvn flyway:validate
```

## Importante

⚠️ **NUNCA** modifique uma migration que já foi aplicada em produção!
⚠️ **SEMPRE** faça backup antes de aplicar migrations em produção
⚠️ Migrations são **irreversíveis** por padrão - planeje com cuidado
