### 1. Install Ollama
### 2. Run llama2:latest locally
### 3. Start this spring boot application
### 4. Test application by following curl command:
  curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Who are you?", "role": "user"}'
