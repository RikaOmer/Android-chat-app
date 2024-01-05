import express from "express";
import tokenServices from "../services/token.js";
const router = express.Router();
router.route("/").post(tokenServices.processLogin); 

export default router;
