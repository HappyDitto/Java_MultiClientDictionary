# Java Multi Client Dictionary
A simple multi thread dictionary implemented based on socket. UI is implemented by Java Swing.

# Features
- After server starts, users can do CRUD operations on a single word.
- All word records are stored in ```dict.json```.
- Server has a GUI that can control the server status(start/stop) and checks the request details (request IP address, operation, operation result).

# Instructions
Start Server
```
Java -jar <path_to_server.jar> <port_number> <path_to_dict.json>
```

Start Client
```
Java -jar <path_to_client.jar> <server_ip_address> <port_number>
```
