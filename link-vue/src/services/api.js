// API服务层，处理与后端的通信

const API_BASE_URL = "http://localhost:8083/desktop/api";

// 通用请求函数
async function request(endpoint, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;

  const defaultOptions = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  const mergedOptions = {
    ...defaultOptions,
    ...options,
  };

  try {
    const response = await fetch(url, mergedOptions);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("API request failed:", error);
    throw error;
  }
}

// 文件上传函数
async function uploadFileRequest(endpoint, file, options = {}) {
  const url = `${API_BASE_URL}${endpoint}`;

  const formData = new FormData();
  formData.append("file", file);

  const mergedOptions = {
    method: "POST",
    body: formData,
    ...options,
  };

  try {
    const response = await fetch(url, mergedOptions);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    return await response.json();
  } catch (error) {
    console.error("File upload failed:", error);
    throw error;
  }
}

// 备忘录API
export const notesApi = {
  // 获取所有备忘录
  getAllNotes: () => request("/notes"),

  // 获取指定状态的备忘录
  getNotesByStatus: (status) => request(`/notes/status/${status}`),

  // 根据ID获取备忘录
  getNoteById: (id) => request(`/notes/${id}`),

  // 添加备忘录
  addNote: (note) =>
    request("/notes", {
      method: "POST",
      body: JSON.stringify(note),
    }),

  // 更新备忘录
  updateNote: (note) =>
    request("/notes", {
      method: "PUT",
      body: JSON.stringify(note),
    }),

  // 删除备忘录
  deleteNote: (id) =>
    request(`/notes/${id}`, {
      method: "DELETE",
    }),
};

// 壁纸API
export const wallpaperApi = {
  // 获取所有壁纸
  getAllWallpapers: () => request("/wallpaper"),

  // 获取当前壁纸
  getCurrentWallpaper: () => request("/wallpaper/current"),

  // 根据ID获取壁纸
  getWallpaperById: (id) => request(`/wallpaper/${id}`),

  // 添加壁纸
  addWallpaper: (wallpaper) =>
    request("/wallpaper", {
      method: "POST",
      body: JSON.stringify(wallpaper),
    }),

  // 更新壁纸
  updateWallpaper: (wallpaper) =>
    request("/wallpaper", {
      method: "PUT",
      body: JSON.stringify(wallpaper),
    }),

  // 删除壁纸
  deleteWallpaper: (id) =>
    request(`/wallpaper/${id}`, {
      method: "DELETE",
    }),

  // 设置当前壁纸
  setCurrentWallpaper: (id) =>
    request(`/wallpaper/current/${id}`, {
      method: "PUT",
    }),
};

// 个性化设置API
export const settingsApi = {
  // 获取所有设置
  getAllSettings: () => request("/settings"),

  // 获取设置Map
  getSettingsMap: () => request("/settings/map"),

  // 根据键获取设置
  getSettingByKey: (key) => request(`/settings/${key}`),

  // 更新单个设置
  updateSetting: (key, value) =>
    request(`/settings/${key}`, {
      method: "PUT",
      body: JSON.stringify({ value }),
    }),

  // 批量更新设置
  updateSettings: (settingsMap) =>
    request("/settings", {
      method: "PUT",
      body: JSON.stringify(settingsMap),
    }),
};

// 文件上传API
export const uploadApi = {
  // 上传文件
  uploadFile: (file) => uploadFileRequest("/upload/file", file),
};

// 桌面API
export const desktopApi = {
  // 获取桌面设置
  getDesktopSettings: () => request("/desktop/settings"),
  
  // 更新桌面设置
  updateDesktopSettings: (settings) => 
    request("/desktop/settings", {
      method: "PUT",
      body: JSON.stringify(settings),
    }),
  
  // 获取桌面图标
  getDesktopIcons: () => request("/desktop/icons"),
  
  // 更新桌面图标
  updateDesktopIcons: (icons) => 
    request("/desktop/icons", {
      method: "PUT",
      body: JSON.stringify(icons),
    }),
  
  // 获取应用列表
  getApps: () => request("/desktop/apps"),
  
  // 启动应用
  launchApp: (appId) => 
    request("/desktop/apps/launch", {
      method: "POST",
      body: JSON.stringify({ appId }),
    }),
  
  // 卸载应用
  uninstallApp: (appId) => 
    request(`/desktop/apps/${appId}`, {
      method: "DELETE",
    }),
};
