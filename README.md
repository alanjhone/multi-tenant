Este projeto é referente a uma arquitetura desenvolvida, agregada à versão 4 do framework Hibernate, que provê mecanismos de suporte à arquitetura Multi-Tenant (vários clientes, logicamente isolados, utilizando os mesmos recursos físicos).

#### Multilocação no Hibernate
Conforme mencionado no [Guia](https://docs.jboss.org/hibernate/orm/5.2/userguide/html_single/Hibernate_User_Guide.html#multitenacy) oficial do usuário do Hibernate , existem três abordagens para a ocupação múltipla no Hibernate:

* **Esquema separado** - um esquema por inquilino na mesma instância de banco de dados físico
* **Banco de dados separado** - uma instância de banco de dados físico separada por inquilino
* **Dados particionados (discriminadores)** - os dados de cada inquilino são particionados por um valor discriminador

Como de costume, o Hibernate abstrai a complexidade em torno da implementação de cada abordagem.

Tudo o que precisamos é fornecer uma implementação dessas duas interfaces:

* **MultiTenantConnectionProvider** - fornece conexões por inquilino

* **CurrentTenantIdentifierResolver** - resolve o identificador de inquilino a ser usado


#### Estratégia de multilocação

A estratégia é usada para especificar que tipo de arquitetura de multilocação está sendo empregada. No nosso caso, vamos usar a *multitenancy* baseada em esquema, precisamos usar o *MultiTenancyStrategy.SCHEMA* e passá-lo através da  propriedade de configuração: *hibernate.multiTenancy*

```console
<prop key="hibernate.multiTenancy">SCHEMA</prop>
```

####  Implementação da MultiTenancyConnectionProvider

Para o Hibernate rotear adequadamente as solicitações de conexão com o esquema do banco de dados ao qual cada usuário está associado, precisamos fornecer uma MultiTenancyConnectionProviderImp por meio da propriedade de configuração: *hibernate.multi_tenant_connection_providerpropriedade*

```console
<prop key="hibernate.multi_tenant_connection_provider">br.com.dev.sysos.helpers.MultiTenantConnectionProviderImpl</prop>
```

#### Implementação da CurrentTenantIdentifierResolver

A implementação da interface CurrentTenantIdentifierResolver será usada para localizar e identificar o inquilino associado ao Thread em execução atual. Então, precisamos definir a propriedade de configuração através da propriedade:

```console
<prop key="hibernate.tenant_identifier_resolver">br.com.dev.sysos.helpers.CurrentTenantIdentifierResolverImpl</prop>
```

