import chat from "../models/chat.js";
import message from "../models/message.js";
import user from "../models/users.js";

const getAllChats = async (username) => {
  const destChats = await chat.find({});
  if (!destChats) throw new Error("No chats found");
  const mainUser = await user.findOne({ username: username });
  const userId = mainUser._id.valueOf();

  const chats = destChats.filter((chat) => {
    return chat.users.includes(userId);
  });

  const finalChatsPromise = chats.map(async (chat) => {
    const otherUser = chat.users.find((user) => user.valueOf() !== userId);
    if (otherUser === undefined) return;
    const other = await user.findById(otherUser.valueOf());
    const msg = await message
      .findById(chat.messages[chat.messages.length - 1])
      .populate("sender")
      .exec()
      .then((msg) => {
        if (!msg) return;
        return {
          content: msg.content,
          sender: msg.sender,
          created: msg.created,
        };
      });
    return {
      id: chat.id,
      user: other,
      lastMessage: msg,
    };
  });

  const finalChats = await Promise.all(finalChatsPromise);
  return finalChats;
};
const getChatById = async (chatId, username) => {
  const currentChat = await chat.findById(chatId);
  if (!currentChat) throw new Error("Chat not found");
  const mainUser = await user.findOne({ username: username });
  const userId = mainUser._id.valueOf();
  const otherUserId = currentChat.users.find(
    (user) => user.valueOf() !== userId
  );
  const otherUser = await user.findById(otherUserId.valueOf());
  return {
    _id: chatId,
    users: [otherUser],
  };
};

const deleteChatById = async (id) => {
  return await chat.findByIdAndDelete(id);
};

const createChat = async (destUsername, srcUsername) => {
  const destUser = await user.findOne({ username: destUsername });
  if (destUser.length === 0) {
    throw new Error("Dest User not found");
  }
  const srcUser = await user.findOne({ username: srcUsername });
  if (srcUser.length === 0) {
    throw new Error("Src User not found");
  }
  const userslist = [{ _id: destUser.id }, { _id: srcUser.id }];
  if (await chat.findOne({ users: { $all: userslist } })) {
    throw new Error("Chat already exists");
  }

  const newChat = new chat({
    users: userslist,
    messages: [],
  });

  return await newChat.save();
};

export default {
  getAllChats,
  getChatById,
  deleteChatById,
  createChat,
};
