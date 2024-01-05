import notificationController from '../controllers/notification.js';

const addUserToken = async (req, res) => {
  try {
    res.json(
      await notificationController.addUserToken(
        req.body.username,
        req.body.token
      )
    );
  } catch (err) {
    res.status(400).json({ error: 'failed to add token' });
  }
};

const getUserToken = async (req, res) => {
  try {
    res.json(await notificationController.getUserToken(req.body.username));
  } catch (err) {
    res.status(400).json({ error: 'failed to get token' });
  }
};

const deleteUserToken = async (req, res) => {
  try {
    res.json(
      await notificationController.deleteUserToken(
        req.body.username,
        req.body.token
      )
    );
  } catch (err) {
    res.status(400).json({ error: 'fail to delete token' });
  }
};
export default { deleteUserToken, getUserToken, addUserToken };
