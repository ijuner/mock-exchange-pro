// src/pages/TradeLive.tsx
import React, { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

type Trade = {
  id: string;
  symbol: string;
  price: number;
  quantity: number;
};

export default function TradeLive() {
  const [trades, setTrades] = useState<Trade[]>([]);

  useEffect(() => {
    const socket = new SockJS("https://your-cloud-run-service/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        client.subscribe("/topic/trades", message => {
          const trade: Trade = JSON.parse(message.body);
          setTrades(prev => [trade, ...prev.slice(0, 19)]);
        });
      },
    });

    client.activate();
    return () => client.deactivate();
  }, []);

  return (
    <div className="p-4">
      <h2 className="text-lg font-semibold mb-4">📈 实时成交记录</h2>
      <ul>
        {trades.map(trade => (
          <li key={trade.id} className="border-b py-2">
            {trade.symbol} - 💰 {trade.price} × 📦 {trade.quantity}
          </li>
        ))}
      </ul>
    </div>
  );
}
