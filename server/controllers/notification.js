import NotificationTable from '../models/notification.js';
import { sendNotification } from '../utils/firebaseActions.js';

const addUserToken = async (username, token) => {
  const user = new NotificationTable({ username, token });
  return await user.save();
};

const getUserToken = async (username) => {
  return await NotificationTable.findOne({ username });
};

const sendToMobileByUsername = async (username, title, message) => {
  const userTokens = await NotificationTable.find({ username });
  userTokens.forEach(async (token) => {
    await sendNotification(token.token, title, message);
  });
};

const deleteUserToken = async (username, token) => {
  const resp = await NotificationTable.deleteOne({ username, token });
  return resp;
};
export default {
  deleteUserToken,
  sendToMobileByUsername,
  addUserToken,
  getUserToken,
};
