# undertow-examples
Examples of using the Undertow Java web server - the web server used inside the JBoss Wildfly application server.

## Undertow Tutorial
The examples in this repository are matching the Undertow tutorial found here:

[https://jenkov.com/tutorials/undertow/index.html](https://jenkov.com/tutorials/undertow/index.html)

## Undertow Examples

- [SimpleHttpServer](https://github.com/jjenkov/undertow-examples/blob/main/src/main/java/com/jenkov/

## How to Build and Run

### 1. Build and Run the Undertow Server

Open a terminal in the project root and run:

```powershell
mvnd clean package
```

To run the Undertow server (make sure the JAR is built):

```powershell
java -cp "target/classes;target/jenkov-com-jar-with-dependencies.jar" com.jenkov.undertowexamples.UndertowHttpServer
```

### 2. Build and Run the React Frontend

Open a terminal in the `frontend` directory:

```powershell
cd frontend
npm install
npm run build
```

This will create a production build in `frontend/build` that is served by the Undertow server.

### 3. Access the Application

Open your browser and go to:

```
http://localhost:9090/
```

- The React app will be served from this address.
- All API calls to `/api/todos` are handled by the Undertow backend.

## Contributions

This project is based on the original [jenkov/undertow-examples](https://github.com/jjenkov/undertow-examples) repository.

Contributions are welcome! If you have improvements, bug fixes, or new examples, please submit a pull request or open an issue.

- Fork the repository
- Create a feature branch
- Commit your changes
- Open a pull request with a clear description

Thank you for helping improve this project!


