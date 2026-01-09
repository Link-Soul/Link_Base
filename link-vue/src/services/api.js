// API服务层，处理与后端的通信

const API_BASE_URL = "/desktop";

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

    // 检查响应是否为空
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      return await response.json();
    } else {
      // 对于空响应或非 JSON 响应，返回 null
      return null;
    }
  } catch (error) {
    console.error("API request failed:", error);
    // 不抛出错误，返回 null 以便前端可以继续执行
    return null;
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

    // 检查响应是否为空
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      return await response.json();
    } else {
      // 对于字符串响应（如直接返回URL），返回包含fileUrl字段的对象
      const text = await response.text();
      if (text) {
        return { fileUrl: text };
      }
      return null;
    }
  } catch (error) {
    console.error("File upload failed:", error);
    // 不抛出错误，返回 null 以便前端可以继续执行
    return null;
  }
}

// 备忘录API
export const notesApi = {
  // 获取所有备忘录
  getAllNotes: () => request("/api/notes"),

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
    request(`/settings/updateSetting`, {
      method: "PUT",
      body: JSON.stringify({
        settingKey: key,
        settingValue: value,
      }),
    }),

  // 批量更新设置
  updateSettings: (settingsMap) =>
    request("/settings/updateSettings", {
      method: "PUT",
      body: JSON.stringify(settingsMap),
    }),
};

// 文件上传API
export const uploadApi = {
  // 上传系统文件
  uploadSysFile: (file) => uploadFileRequest("/sysFiles", file),
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
