import express from 'express';
import mongoose from 'mongoose';
import bodyParser from 'body-parser';
import users from './routes/users.js';
import token from './routes/token.js';
import notification from './routes/notification.js';
import chats from './routes/chats.js';
import cors from 'cors';
import http from 'http';
import fs from 'fs';
import { Server } from 'socket.io';
import { fileURLToPath } from 'url';
import { dirname } from 'path';
import message from './models/message.js';
import { sendNotification } from './utils/firebaseActions.js';
const uri =
  'mongodb+srv://admin:admin@boyadb.kpr6cj2.mongodb.net/?retryWrites=true&w=majority';

mongoose.connect(uri, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
});

const app = express();
app.use(cors());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.use('/api/Users', users);
app.use('/api/Tokens', token);
app.use('/api/chats', chats);
app.use('/api/Notification', notification);
app.use(express.static('public/build'));
// const server = http.createServer(app);
// const io = new Server(server, {
//   cors: {
//     origin: "*",
//     methods: ["GET", "POST"],
//     allowedHeaders: ["my-custom-header"],
//     credentials: true,
//   },
// });

function sendTime() {
  // console.log(new Date().toJSON());
  io.emit('newMessage', { time: new Date().toJSON() });
}

// setInterval(sendTime, 10000);

const io = new Server(2501, {
  cors: {
    origin: '*',
    methods: ['GET', 'POST'],
    allowedHeaders: ['my-custom-header'],
    credentials: true,
  },
});
io.on('connection', (socket) => {
  console.log('a user connected');
  socket.on('message_sent', (data) => {
    console.log(data);
    socket.broadcast.emit('message_received', data);
  });
  socket.on('disconnect', () => {
    console.log('A user disconnected');
  });
});

// io.on("message_sent", (data) => {
//   console.log(data + "hi");
//   // io.broadcast.emit("message_received", data);
// });
const port = 2500;
app.listen(port, () => {
  console.log(`Server listening on port ${port}`);
});
