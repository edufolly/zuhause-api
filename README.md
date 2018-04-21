# zuhause-api

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/0822e76a97d644d2afcefc032e9453d5)](https://app.codacy.com/app/edufolly/zuhause-api?utm_source=github.com&utm_medium=referral&utm_content=edufolly/zuhause-api&utm_campaign=badger)

Java API for IoT on Raspberry Pi


###### Chaves
**monitora_mac**
MAC Address que são monitorados.

**mac_status**
Status dos dispositivos monitorados pelo MAC Address.

**resolve_mac**
Apelidos para resolver o MAC Address.

**telegram_bot**
*zuhause_iot_bot* -> Offset das mensagens.
*send_to* -> Para onde enviar.

**temp**
Registro de temperatura.

**ignore_device**
*name* -> Ignora através do nome.
*mac* -> Ignora através do MAC Address.

**sunrise_sunset**
*lat* -> Latitude
*lng* -> Longitude
*name* -> Nome na mensagem e controle de ligado e desligado do Arduino.
*pin* -> Pino referente ao acionamento no arduino.