import mongoose from "mongoose";
// import user from "./user";
import message from "./message.js";
const scheme = mongoose.Schema;
const chat = new mongoose.Schema({
  users: [{ type: mongoose.Schema.Types.ObjectId, ref: "User" }],
  messages: [{ type: mongoose.Schema.Types.ObjectId, ref: "message" }],
});

export default mongoose.model("chat", chat);
