import chatController from "../controllers/chat.js";
import verifyToken from "../utils/tokenVerifier.js";
const deleteChatById = async (req, res) => {
  const token = req.headers.authorization;
  const username = verifyToken.verify(token);
  if (!username) {
    res.status(401).end();
    return;
  }
  res.json(await chatController.deleteChatById(req.params.id));
};
const getChatById = async (req, res) => {
  const token = req.headers.authorization;
  const username = verifyToken.verify(token);
  if (!username) {
    res.status(401).end();
    return;
  }
  res.json(await chatController.getChatById(req.params.id, username));
};

const getAllChats = async (req, res) => {
  const token = req.headers.authorization;
  const username = verifyToken.verify(token);
  if (!username) {
    res.status(401).end();
    return;
  }
  res.json(await chatController.getAllChats(username));
};

const createChat = async (req, res) => {
  const token = req.headers.authorization;
  const username = verifyToken.verify(token);
  if (!username) {
    res.status(401).end();
    return;
  }
  try {
    res
      .status(200)
      .json(await chatController.createChat(username, req.body.username));
  } catch (err) {
    res.status(400);
    res.json({ error: err });
  }
};

export default {
  getAllChats,
  getChatById,
  deleteChatById,
  createChat,
};
