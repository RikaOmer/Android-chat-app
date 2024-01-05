import express from "express";
import chatServices from "../services/chat.js";
import msgServices from "../services/message.js";
const routes = express.Router();
routes.route("/").get(chatServices.getAllChats).post(chatServices.createChat);
routes
  .route("/:id")
  .get(chatServices.getChatById)
  .delete(chatServices.deleteChatById);
routes
  .route("/:id/Messages")
  .get(msgServices.getMsgs)
  .post(msgServices.createMsg);

export default routes;
