import React, { useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const { user, token, logout } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      navigate("/"); // 未登录用户重定向回登录页
    }
  }, [token, navigate]);

  if (!user) return <div className="text-center mt-10">Loading user info...</div>;

  return (
    <div className="max-w-md mx-auto mt-20 bg-white shadow-md rounded p-6">
      <h2 className="text-xl font-semibold mb-4">👤 Welcome, {user.username}</h2>
      <p><strong>Email:</strong> {user.email}</p>
      <p><strong>User ID:</strong> {user.id}</p>

      <button
        onClick={() => {
          logout();
          navigate("/");
        }}
        className="mt-6 w-full bg-red-500 text-white py-2 rounded"
      >
        Logout
      </button>
    </div>
  );
}
