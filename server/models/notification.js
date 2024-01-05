import mongoose from "mongoose";

const scheme = mongoose.Schema;

const NotificationTable = new scheme({
  username: {
    type: String,
    required: true,
    unique: true,
  },
  token: {
    type: String,
    required: true,
  },
});

export default mongoose.model("NotificationTable", NotificationTable);