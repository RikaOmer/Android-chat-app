import UserPass from "../models/userPass.js";

const isExists = async (username, password) => {
  let isUserExists = false;
  await UserPass.findOne({ username: username, password: password }).then(
    (user) => {
      if (user) {
        isUserExists = true;
      }
    }
  );
  return isUserExists;
};

export default isExists;
