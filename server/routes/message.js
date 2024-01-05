import express from "express";
import msgServices from "../services/message.js";
const router = express.Router();

router
  .route("/:id/Messages")
  .get(msgServices.getMsgs)
  .post(msgServices.createMsg);

export default router;
