import userController from "../controllers/users.js";
import verifyToken from "../utils/tokenVerifier.js";
const createUser = async (req, res) => {
  try {
    res.json(
      await userController.saveUser(
        req.body.username,
        req.body.displayName,
        req.body.profilePic,
        req.body.password
      )
    );
  } catch (err) {
    res.status(400).json({ error: "username already exists" });
  }
};

const getUser = async (req, res) => {
  const token = req.headers.authorization;
  const username = verifyToken.verify(token);
  if (!username) {
    res.status(401).end();
    return;
  }
  res.json(await userController.getUserByUsername(req.params.username));
};

const getUsers = async (req, res) => {
  const token = req.headers.authorization;
  const username = verifyToken.verify(token);
  if (!username) {
    res.status(401).end();
    return;
  }
  res.json(await userController.getUsersAllUsers());
};

export default { createUser, getUser, getUsers };
