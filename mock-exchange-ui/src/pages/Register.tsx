import React, { useState } from "react";
import { register } from "../api/auth";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const navigate = useNavigate();
  const [form, setForm] = useState({ username: "", email: "", password: "" });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await register(form.username, form.email, form.password);
      alert("Register success");
      navigate("/login");
    } catch (err) {
      alert("Register failed");
    }
  };

  return (
    <div className="max-w-sm mx-auto mt-20">
      <h2 className="text-xl font-bold mb-4">Register</h2>
      <form onSubmit={handleSubmit} className="space-y-4">
        {["username", "email", "password"].map((field) => (
          <input
            key={field}
            name={field}
            placeholder={field}
            type={field === "password" ? "password" : "text"}
            value={form[field as keyof typeof form]}
            onChange={handleChange}
            className="w-full p-2 border"
          />
        ))}
        <button type="submit" className="bg-green-500 text-white w-full py-2">Register</button>
      </form>
    </div>
  );
}
