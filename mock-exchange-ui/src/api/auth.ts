import API from "./axiosInstance";

export const login = async (email: string, password: string) => {
  const response = await API.post("/login", { email, password });
  return response.data; // 包含 token
};

export const register = async (username: string, email: string, password: string) => {
  const response = await API.post("/register", { username, email, password });
  return response.data;
};

export const getMe = async (token: string) => {
  const response = await API.get("/me", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return response.data;
};
