import api from "../api";

export const signup = (email, password) => api.post("/signup", { email, password });
export const login = (email, password) => api.post("/login", { email, password });
export const logout = () => api.post("/logout");
export const getProfile = () => api.get("/me");
