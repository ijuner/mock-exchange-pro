// src/pages/Orders.tsx
import React, { useEffect, useState } from "react";
import axios from "axios";

type Order = {
  id: string;
  price: number;
  quantity: number;
};

export default function Orders() {
  const [orders, setOrders] = useState<Order[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);

  const loadOrders = async () => {
    const res = await axios.get(`/api/orders?page=${page}&size=10`);
    setOrders(prev => [...prev, ...res.data.content]);
    setHasMore(page + 1 < res.data.totalPages);
  };

  useEffect(() => {
    loadOrders();
  }, [page]);

  const handleScroll = (e: React.UIEvent<HTMLDivElement>) => {
    const { scrollTop, scrollHeight, clientHeight } = e.currentTarget;
    if (scrollTop + clientHeight >= scrollHeight - 10 && hasMore) {
      setPage(p => p + 1);
    }
  };

  return (
    <div className="h-screen overflow-y-auto p-4" onScroll={handleScroll}>
      {orders.map(order => (
        <div key={order.id} className="border-b py-2">
          💰 Price: {order.price} | 📦 Quantity: {order.quantity}
        </div>
      ))}
      {!hasMore && <div className="text-center py-4 text-gray-500">No more orders</div>}
    </div>
  );
}
