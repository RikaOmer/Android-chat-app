import msgController from '../controllers/message.js';
import verifyToken from '../utils/tokenVerifier.js';
const createMsg = async (req, res) => {
  try {
    const token = req.headers.authorization;
    const username = verifyToken.verify(token);
    if (!username) {
      res.status(401).end();
      return;
    }
    res.json(
      await msgController.saveMsg(req.body.msg, username, req.params.id)
    );
  } catch (err) {
    res.status(500).send(err.message);
  }
};

const getMsgs = async (req, res) => {
  try {
    const token = req.headers.authorization;
    const username = verifyToken.verify(token);
    if (!username) {
      res.status(401).end();
      return;
    }
    const resp = await msgController.getAllMsgs(req.params.id);
    if (resp) {
      res.json(resp);
    } else res.status(404).send('Not found');
  } catch (err) {
    res.status(500).send(err.message);
  }
};

export default { createMsg, getMsgs };
