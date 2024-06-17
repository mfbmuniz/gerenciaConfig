Feature: Login
  Scenario: Faz Login como Admin
    Given LoginRequest solicitando login
      | admin@admin.com    | 12345678 |
    When quando botao login for pressionado
    Then response status code for login is 302
  Scenario: Lista Usuarios
    When the client calls endpoint "/page/0/size/10"
    Then response status code is 200