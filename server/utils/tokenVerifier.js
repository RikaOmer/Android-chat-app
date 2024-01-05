import jwt from "jsonwebtoken";
const verify = (userToken) => {
  // If the request has an authorization header
  const key = "B0ya s3cr4t K2y!!!!!";
  if (userToken) {
    const token = userToken.split(" ")[1];
    try {
      const data = jwt.verify(token, key);
      return data.username; // Token validation was successful.
    } catch (err) {
      return null;
    }
  } else return null;
};

export default { verify };
