<table>
  <tr>
    <td>
      <img src="https://github.com/dmm76/tcc_senac/blob/main/src/main/java/util/images/BR_Sistema_LOGO2.png?raw=true" alt="Logo_BR" width="150">
    </td>
    <td>
      <h1>Sistema Bar e Restaurantes (Senac)</h1>
    </td>
  </tr>
</table>

Este repositório contém o projeto de Trabalho de Conclusão de Curso (Projeto Integrador) desenvolvido para a professora **Claudia Tupan**, do curso de **Desenvolvimento de Sistemas - Senac**.

![Diagrama DDR](https://github.com/dmm76/projeto_integrador_JFrame/blob/main/src/main/java/util/images/DDR_Banco.png?raw=true)


## 📌 Descrição

O projeto consiste em um **sistema para bar e restaurantes simulado**, implementado em **Java**.
Possui as funcionalidades básicas de gerenciamento de pdv de vendas.
Possui também funcionalidade de cadastro de clientes, produtos, fornecedores e etc).

## 🛠️ Tecnologias Utilizadas

- Java 21
- Maven
- MySQL
- JPA / Hibernate
- JDBC
- JavaFX (opcional, se houver interface gráfica)
- Git e GitHub

## 📁 Estrutura do Projeto

** Diagrama de Classe Sistema BR **

![Diagrama Classe](https://github.com/dmm76/projeto_integrador_JFrame/blob/main/src/main/java/util/images/DiagramaClasseSistemaBR.png?raw=true)

## 🔒 Segurança de Credenciais com `.env`

Para proteger as informações sensíveis (usuário e senha do banco de dados), este projeto utiliza variáveis de ambiente via arquivo `.env`, que **não é incluído no controle de versão (GitHub)**.

### 📌 Como configurar

1. **Crie um arquivo `.env` na raiz do projeto** com o seguinte conteúdo:
2. **Crie a classe EnvLoader**
3. **Modifique a classe JPAUtil** removendo as linha que fazem o link com o banco de dados
```env
DB_USER=seuUsuarioAqui
DB_PASSWORD=suaSenhaAqui

✅ Benefícios

    Maior segurança (nenhuma senha fica exposta no projeto)

    Maior portabilidade (fácil troca de credenciais entre ambientes)

    Evita vazamentos acidentais no GitHub ou versionamento
