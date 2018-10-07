## Login

POST /user
```
{
    "email": "string",
    "password": "string"
}
```

Validações:
- Email está cadastrado?
- Senha está certa?

200
```
{
    "userToken": "string" (pode ser o id do usuario, algo que dê pra buscar as infos dele)
}
```
--

## Register
POST /user/register
```
{
    "email": "string",
    "password": "string"
    "checkPassword": "string"
}
```

Validações:
- Email já foi usado?
- Senha e confirmação da senha são iguais? (opcional, vou fazer essa validação no app também)

200
```
{
    "userToken": "string" (pode ser o id do usuario, algo que dê pra buscar as infos dele)
}
```
--

## Join session

### Join as guest
POST /sessions/join

```
{
    "sessionId": "string"
}
```

Validações:
- Sessão existe?

--

### Join as user
POST /sessions/join

```
{
    "sessionId": "string"
    "userToken": "string" (ou email do usuario, mas pref ser um token/id)
}
```

Validações:
- Sessão existe?
- Sessão está ativa?
- Email é de algum usuário?

--

## Create session
POST /sessions

```
{
    "userToken": "string" (ou email mas pref token/id)
}
```
Validações:
- Email está cadastrado?
- Usuário está conectado em alguma sessão?

200
```
{
    "sessionId": "string"
}
```

## Fetch sessions
GET /sessions/

```
{
    "userToken": "string" (ou email mas pref token/id)
}
```

Validações:
- Token é valido? (usuario existe?)

200
```
{
    "sessions": [
        "string",
        "string",
        ...
        "string"
    ]
}
```
--

## React/comment on session
POST /sessions/comment

```
{
    "sessionId": "string",
    "reaction": "string",
    "comment": "string" (optional)
}
```

Validações:
- Sessão existe?
- Sessão está ativa?


--

## Fetch comments
GET /sessions/comment

```
{
    "email": "string",
    "sessionId": "string"
}
```

Validações:
- Usuário existe?
- Usuário é dono da sessão?

200
```
[
    {
        "reaction": "string",
        "comment": "string" (optional)
    },
    {...},
    {...},
    .
    .
    {...}
]
```
--

## Tratamento de erros

Caso ocorra algum erro retornar status do erro e um json no seguinte formato
```
{
    "message": "string"
}
```
com uma mensagem descrevendo pq ocorreu o erro.
Obs.: é bom ter uma mensagem padrão pra caso passe por todas validações mas ainda dê erro, algo como
```
{
    "message": "Oops! Algo deu errado"
}
```