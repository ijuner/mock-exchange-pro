import axios from "axios";

const API = axios.create({
  baseURL: "https://auth-service.a.run.app/api/auth", // Cloud Run URL
  headers: {
    "Content-Type": "application/json",
  },
});

export default API;
