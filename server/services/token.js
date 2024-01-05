import tokenController from "../controllers/token.js";

const LoggedIn = async (req, res) => {
  res.send(
    await tokenController.LoggedIn(req.body.username, req.body.password)
  );
};

const processLogin = async (req, res) => {
  try {
    const token = await tokenController.processLogin(
      req.body.username,
      req.body.password
    );
    res.send(token);
  } catch (err) {
    res.status(400).send(err.message);
  }
};

// const index = (req, res) => {
// res.json({ data: 'secret data' }) /// ??????????????????????????????
// }

export default { LoggedIn, processLogin };
